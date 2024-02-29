package com.yeohangttukttak.api.domain.file;

import com.yeohangttukttak.api.domain.place.entity.Location;
import com.yeohangttukttak.api.domain.place.entity.Place;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    @Nested
    class 파일_첨부 {

        @Test
        public void 성공() throws Exception {
            // given
            File file = File.builder().id(1L).build();
            Place place = Place.builder()
                    .id(1L)
                    .location(new Location(36.6665, 127.4945))
                    .build();

            // when
            file.attach(place);

            // then

            assertEquals(file.getSourceId(), place.getId(), "파일에 장소가 올바르게 첨부되어야 한다.");
            assertTrue(place.getFiles().contains(file), "장소에 파일이 올바르게 첨부되어야 한다.");
        }

        @Test
        public void 첨부파일_존재_실패() throws Exception {
            // given
            File file = File.builder().id(1L).build();
            Place placeA = Place.builder().id(1L)
                    .location(new Location(36.6665, 127.4945))
                    .build();
            file.attach(placeA);

            Place placeB = Place.builder().id(2L)
                    .location(new Location(36.6665, 127.4945))
                    .build();

            // when, then
            assertThrows(IllegalStateException.class, () ->
                    file.attach(placeB));

        }

        @Test
        public void 첨부_식별자_없음_실패() throws Exception {
            // given
            File file = File.builder().id(1L).build();
            Place place = Place.builder()
                    .location(new Location(36.6665, 127.4945))
                    .build();

            // when, then
            assertThrows(IllegalArgumentException.class, () ->
                    file.attach(place));

        }


    }
}