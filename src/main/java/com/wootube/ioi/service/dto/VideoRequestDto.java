package com.wootube.ioi.service.dto;

import com.wootube.ioi.service.exception.DescriptionMaxLengthException;
import com.wootube.ioi.service.exception.TitleMaxLenthException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoRequestDto {
    private String title;
    private String description;

    public VideoRequestDto(String title, String description) {
        checkTextLength(title, description);
        this.title = title;
        this.description = description;
    }

    private void checkTextLength(String title, String description) {
        if (title.length() > 50) {
            throw new TitleMaxLenthException();
        }
        if (description.length() > 1000) {
            throw new DescriptionMaxLengthException();
        }
    }
}
