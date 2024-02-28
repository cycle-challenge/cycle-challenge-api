package com.yeohangttukttak.api.converter;

import com.yeohangttukttak.api.domain.place.Location;
import com.yeohangttukttak.api.domain.place.LocationDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringToLocationDTOConverterTest {

    private final StringToLocationDTOConverter converter = new StringToLocationDTOConverter();

    @Nested
    class 성공_케이스 {

        @Test
        public void 위도_경도() {
            // given
            String pointString = "36.6416,127.4849";

            // when
            LocationDTO location = converter.convert(pointString);

            // then
            assertNotNull(location, "객체가 null이 아니어야 한다.");
            assertEquals(36.6416, location.getLatitude(), 0.0001, "위도는 36.6416 이어야 한다.");
            assertEquals(127.4849, location.getLongitude(), 0.0001, "경도는 127.4849");
        }

        @Test
        public void 위도_경도_공백() {
            // given
            String pointString = "36.6416 , 127.4849";

            // when
            LocationDTO location = converter.convert(pointString);

            // then
            assertNotNull(location, "객체가 null이 아니어야 한다.");
            assertEquals(36.6416, location.getLatitude(), 0.0001, "위도는 36.6416 이어야 한다.");
            assertEquals(127.4849, location.getLongitude(), 0.0001, "경도는 127.4849");
        }
    }

    @Nested
    class 토큰_개수_예외 {

        @Test
        public void 토큰_개수_부족_실패() {
            // given
            String pointString = "36.6416,";

            // when, then
            assertThrows(IllegalArgumentException.class, () ->
                    converter.convert(pointString));
        }

        @Test
        public void 토큰_개수_초과_실패() {
            // given
            String pointString = "36.6416,127.4849,127.4849";

            // when, then
            assertThrows(IllegalArgumentException.class, () ->
                    converter.convert(pointString));}

        @Test
        public void 콤마_없음_실패() {
            // given
            String pointString = "36.6416127.4849";

            // when, then
            assertThrows(IllegalArgumentException.class, () ->
                    converter.convert(pointString));}
    }

    @Nested
    class 값_형식_예외 {

        @Test
        public void 정수_쉼표_구분_성공() {
            // given
            String pointString = "36,127";

            // when
            LocationDTO location = converter.convert(pointString);

            // then
            assertNotNull(location, "객체가 null이 아니어야 한다.");
            assertEquals(36.0, location.getLatitude(), 0.0001, "위도는 36.0 이어야 한다.");
            assertEquals(127.0, location.getLongitude(), 0.0001, "경도는 127.0");
        }

        @Test
        public void 문자열_실패() {
            // given
            String pointString = "안녕,127";

            // when, then
            assertThrows(NumberFormatException.class, () ->
                    converter.convert(pointString));
        }
    }
}