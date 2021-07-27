package com.company;
import com.revenueengine.collector.enumeration.CollectorMessages;
import com.revenueengine.collector.event.CollectorDataResponseEvent;
import com.revenueengine.collector.event.CollectorEmptyResponseEvent;
import com.revenueengine.collector.event.system.CollectorParsingExceptionEvent;
import com.revenueengine.collector.model.Connector;
import com.revenueengine.collector.model.FeedStatistic;
import com.revenueengine.collector.service.client.flag.OneAccountPlatform;
import com.revenueengine.collector.service.client.flag.OneBrandPlatform;
import com.revenueengine.kafka.constant.EventPublisherBeanNames;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class Affgoldmine implements PlatformClient, OneBrandPlatform, OneAccountPlatform {

    @Autowired
    @Qualifier(EventPublisherBeanNames.SPRING_APPLICATION_EVENT_PUBLISHER)
    private ApplicationEventPublisher applicationEventPublisher;

    public static class Holder {
        public static final Affgoldmine HOLDER_INSTANCE = new OneWinClient();
    }

    @Override
    public List<FeedStatistic> importData(Connector connector, LocalDate fromDate, LocalDate toDate) {
        Connection.Response reportResponse;
        Connection.Response authResponse = null;

        try {
            authResponse = getAuthorization(connector);
            applicationEventPublisher.publishEvent(new CollectorEmptyResponseEvent(authResponse, connector));
        } catch (Exception e) {
            return processError("Affgoldmine client authorization error", authResponse, connector, e);
        }

        try {
            reportResponse = getReport(authResponse, connector, fromDate, toDate);
            applicationEventPublisher.publishEvent(new CollectorEmptyResponseEvent(authResponse, connector));
        } catch (Exception e) {
            return processError("Affgoldmine client get report error", authResponse, connector, e);
        }

        try {
            var result = parseReport(reportResponse);
            applicationEventPublisher.publishEvent(new CollectorDataResponseEvent(reportResponse, connector, result));
            return result;
        } catch (Exception e) {
            return processError("Affgoldmine client parse error", authResponse, connector, e);
        }
    }

    @SneakyThrows
    private Connection.Response getAuthorization(Connector connector) {
        String authUrl = connector.getAuthUrl();
        return Jsoup.connect(authUrl)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .data("login", connector.getUsername())
                .data("password", connector.getPassword())
                .validateTLSCertificates(false)
                .timeout(60 * 6000)
                .execute();
    }

    @SneakyThrows
    private Connection.Response getReport(Connection.Response authResponse, Connector connector, LocalDate fromDate, LocalDate toDate) {
        String reportUrl = connector.getReportUrl();

        Map<String, String> cookies = authResponse.cookies();
        long from = fromDate.toEpochSecond(LocalTime.MIN, ZoneOffset.MIN) * 1000;
        long to = toDate.toEpochSecond(LocalTime.MIN, ZoneOffset.MIN) * 1000;

        return Jsoup.connect(reportUrl)
                .cookies(cookies)
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .validateTLSCertificates(true)
                .timeout(60 * 6000)
                .data("day", String.format("%d,%d", from, to))
                .execute();
    }


}
