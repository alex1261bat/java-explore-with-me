package ru.practicum.ewm.statistic.model;

import dto.HitEndpointDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StatisticMapper {

    @Mapping(source = "timestamp",
             target = "timestamp",
             dateFormat = "yyyy-MM-dd HH:mm:ss")
    HitEndpointDto toHitEndpointDto(HitEndpoint hitEndpoint);

    @Mapping(source = "timestamp",
             target = "timestamp",
             dateFormat = "yyyy-MM-dd HH:mm:ss")
    HitEndpoint toHitEndpoint(HitEndpointDto hitEndpointDto);
}
