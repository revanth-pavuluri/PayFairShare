package com.payfairshare.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.payfairshare.app.dto.FinalSplitDto;
import com.payfairshare.app.models.FinalSplit;

@Component
@Mapper(componentModel = "spring")
public interface FinalSplitMapper {
    FinalSplitMapper INSTANCE = Mappers.getMapper(FinalSplitMapper.class);

    @Mapping(source = "payTo", target = "payTo.id")
    @Mapping(source = "payBy", target = "payBy.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "status", target = "status")
    FinalSplitDto finalSplitToFinalSplitDto(FinalSplit finalSplit);

    @Mapping(source = "payTo.id", target = "payTo")
    @Mapping(source = "payBy.id", target = "payBy")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "status",target = "status", defaultValue = "Pending")
    @Mapping(target = "id", ignore = true)  
    @Mapping(target = "createdOn", ignore = true) 
    @Mapping(target = "updatedOn", ignore = true)  
    @Mapping(target = "group", ignore = true) 
    @Mapping(target = "groupId", source = "groupId")
    FinalSplit finalSplitDtoToFinalSplit(FinalSplitDto finalSplitDto);
    
    List<FinalSplitDto> toListFinalsplitDto(List<FinalSplit> finalSplits);
    
}
