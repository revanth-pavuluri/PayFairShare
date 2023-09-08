package com.payfairshare.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.payfairshare.app.dto.UserNameDto;
import com.payfairshare.app.dto.UserRequestDto;
import com.payfairshare.app.dto.UserResponseDto;
import com.payfairshare.app.models.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
     @Mappings({
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "username", target = "username"),
        @Mapping(target = "password"),
        @Mapping(target = "userGroups", ignore = true),
        @Mapping(target = "expenses", ignore = true), 
        @Mapping(target = "createdOn", ignore = true), 
        @Mapping(target = "updatedOn", ignore = true) ,
        @Mapping(target = "id" , ignore = true),
        @Mapping(target = "upiId", source = "upiId")
    })
    User userRequestDtoToUser(UserRequestDto userRequestDto);

    @Mapping(target="expenses",ignore=true )
    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> userToUserResponseDtos(List<User> users);

    UserNameDto toUserNameDto(User user);
}
