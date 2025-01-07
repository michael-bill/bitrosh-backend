package com.bitrosh.backend.cofiguration;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public <D, T> D map(T source, Class<D> destinationType) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, destinationType);
    }

    public <S, D> Page<D> map(Page<S> sourcePage, Class<D> destinationType) {
        List<D> destinationList = sourcePage.getContent().stream()
                .map(entity -> map(entity, destinationType)).toList();
        return new PageImpl<>(destinationList, sourcePage.getPageable(), sourcePage.getTotalElements());
    }
}
