package ru.practicum.ewm.statistic.model;

import dto.HitEndpointDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatisticMapper {
    StatisticMapper INSTANCE = Mappers.getMapper(StatisticMapper.class);

    HitEndpointDto toHitEndpointDto(HitEndpoint hitEndpoint);

    HitEndpoint toHitEndpoint(HitEndpointDto hitEndpointDto);
}
