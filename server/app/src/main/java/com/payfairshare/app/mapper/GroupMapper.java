package com.payfairshare.app.mapper;



import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.payfairshare.app.dto.GroupDto;
import com.payfairshare.app.dto.UserNameDto;
import com.payfairshare.app.models.Group;
import com.payfairshare.app.models.User;


@Mapper(uses = {ExpenseMapper.class},componentModel = "spring")
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);
    UserNameDto toUserNameDto(User user);
    List<UserNameDto> toUserNameDtos(List<User> users);
    
    @Mapping(target = "expenses", ignore = true)
    GroupDto toGroupDto(Group group);

    List<GroupDto> toGroupDtos(List<Group> group);

}
