package com.felxx.park_rest_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.felxx.park_rest_api.web.dto.PageableDto;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
