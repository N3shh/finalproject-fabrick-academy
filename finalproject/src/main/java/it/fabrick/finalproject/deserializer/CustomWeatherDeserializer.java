//package it.fabrick.finalproject.deserializer;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.ObjectCodec;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import it.fabrick.finalproject.dto.WeatherResponseClientDto;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//public class CustomWeatherDeserializer extends StdDeserializer<WeatherResponseClientDto> {
//
//    public CustomWeatherDeserializer() {
//        this(null);
//    }
//
//    protected CustomWeatherDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    @Override
//    public WeatherResponseClientDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        WeatherResponseClientDto weatherResponseClientDto = new WeatherResponseClientDto();
//
//        ObjectCodec codec = jsonParser.getCodec();
//        JsonNode jsonNode = codec.readTree(jsonParser);
//
//        List<String> timeDaily = jsonNode.get("daily").findValuesAsText("time");
//        List<String> weatherCode = jsonNode.get("daily").findValuesAsText("weather_code");
//        List<String> timeHourly = jsonNode.get("hourly").findValuesAsText("time");
//        List<String> temperature = jsonNode.get("hourly").findValuesAsText("temperature_2m");
//
//
//        Optional.ofNullable(timeDaily)
//                .ifPresent(weatherResponseClientDto::setTime);
//        Optional.ofNullable(weatherCode)
//                .ifPresent(weatherResponseClientDto::setWeatherCode);
//        Optional.ofNullable(timeHourly)
//                .ifPresent(weatherResponseClientDto::setTime);
//        Optional.ofNullable(temperature)
//                .ifPresent(weatherResponseClientDto::setTemperature);
//
//    }
//}
