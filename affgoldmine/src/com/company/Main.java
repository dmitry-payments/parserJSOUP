package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
    }
}

    @Qualifier(EventPublisherBeanNames.SPRING_APPLICATION_EVENT_PUBLISHER)
    private ApplicationEventPublisher applicationEventPublisher;
    //какой-то enum. EventPublisherBeanNames SPRING_APPLICATION_EVENT_PUBLISHER ApplicationEventPublisher
    // что это такое

public interface PlatformClient {

    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    default List<FeedStatistic> importData(Connector connector, LocalDate fromDate, LocalDate toDate) {
        return new ArrayList<>();
    }

    default List<CohortDataWrapper> importCohorts(Connector connector, LocalDate fromDate, LocalDate toDate) {
        return new ArrayList<>();
    }

}

//configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); конфигуре это метод класса обжектмаппер?
// FAIL_ON_UNKNOWN_PROPERTIES - что такое? если метод, почему вызывается без скобок?
// что такое default? default List<FeedStatistic> importData - список объектов класса feedstatistic?


//public static class Holder {
//    public static final OneWinClient HOLDER_INSTANCE = new OneWinClient();
//} это инициализация без конструктора?

//@Override
//public List<FeedStatistic> importData(Connector connector, LocalDate fromDate, LocalDate toDate) {
//    Connection.Response reportResponse;
//    Connection.Response authResponse = null;
//
//    try {
//        authResponse = getAuthorization(connector);
//        applicationEventPublisher.publishEvent(new CollectorEmptyResponseEvent(authResponse, connector));
//    } catch (Exception e) {
//        return processError("1win client authorization error", authResponse, connector, e);
//    }
//
//    try {
//        reportResponse = getReport(authResponse, connector, fromDate, toDate);
//        applicationEventPublisher.publishEvent(new CollectorEmptyResponseEvent(authResponse, connector));
//    } catch (Exception e) {
//        return processError("1win client get report error", authResponse, connector, e);
//    }
//
//    try {
//        var result = parseReport(reportResponse);
//        applicationEventPublisher.publishEvent(new CollectorDataResponseEvent(reportResponse, connector, result));
//        return result;
//    } catch (Exception e) {
//        return processError("1win client parse error", authResponse, connector, e);
//    }
//}
// список объектов. а куда попали вещи которые инициализированы?


//    private List<FeedStatistic> parseReport(Connection.Response reportResponse) {
//        JSONObject jsonResponse = new JSONObject(reportResponse.body());
//        JSONArray jsonArray = jsonResponse.getJSONArray("days");
//        List<FeedStatistic> feedStatisticList = new ArrayList<>();
//        jsonArray.forEach(o -> {
//            JSONObject jsonObject = (JSONObject) o;
//            feedStatisticList.add(
//                    FeedStatistic.builder()
//                            .clicks(jsonObject.getLong("day_visits"))
//                            .registrations(jsonObject.getLong("day_regs"))
//                            .ndc(jsonObject.getLong("day_new_deposit"))
//                            .deposits(jsonObject.getBigDecimal("day_deposits_sum"))
//                            .netRevenue(jsonObject.getBigDecimal("day_difference"))
//                            .build()
//            );
//        });
//        return feedStatisticList;
//    }
