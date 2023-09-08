package com.payfairshare.app.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapping;

import com.payfairshare.app.dto.ExpenseDto;
import com.payfairshare.app.dto.ExpenseSplitDto;
import com.payfairshare.app.dto.UserNameDto;
import com.payfairshare.app.models.Expense;
import com.payfairshare.app.models.ExpenseSplit;
import com.payfairshare.app.models.User;

@Component
@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);


    @Mappings({
        @Mapping(source = "name", target = "name"),
        @Mapping(target = "username", ignore = true),
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "userGroups", ignore = true),
        @Mapping(target = "expenses", ignore = true), 
        @Mapping(target = "createdOn", ignore = true), 
        @Mapping(target = "updatedOn", ignore = true) ,
        @Mapping(target = "id" , source = "id"),
        @Mapping(target = "upiId", ignore = true)
    })
    User UserNameDtotoUser(UserNameDto userNameDto);

    @Mappings({
        @Mapping(target = "paidBy", ignore = true),
        @Mapping(target = "group.id", source = "groupId"), 
        @Mapping(target = "userId", source = "paidBy.id"),
        @Mapping(target = "status", defaultValue = "Pending"),
        @Mapping(target = "createdOn", ignore = true), 
        @Mapping(target = "updatedOn", ignore = true) ,
        @Mapping(target = "id" , ignore = true),
        @Mapping(target = "expenseSplits",ignore = true),
        @Mapping(target = "usersInSplit",ignore = true)
    })
    Expense toExpense(ExpenseDto expenseDTO);
    @Mappings({
        @Mapping(target = "userId", source = "splitDto.userId"),
        @Mapping(target = "createdOn", ignore = true), 
        @Mapping(target = "updatedOn", ignore = true),
        @Mapping(target = "expense", source = "expense"),
        @Mapping(target = "status", defaultValue = "Pending", source = "splitDto.status"),
        @Mapping(target = "expenseId",source = "expense.id"),
        @Mapping(target = "user", ignore = true),
        @Mapping(target = "id", ignore = true)
    })
    ExpenseSplit toExpenseSplit(ExpenseSplitDto splitDto,Expense expense);
    
    // @Mapping(target = "userId" , source = "user.id")
    ExpenseSplitDto toExpenseSplitDto(ExpenseSplit expenseSplit);


    List<ExpenseSplitDto> toExpenseSplitDtos(List<ExpenseSplit> expenseSplits);

    @Mapping(target = "paidBy.id", source = "paidBy.id")
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "paidBy.name", source = "paidBy.name")
    @Named("toExpenseDto")
    ExpenseDto toExpenseDto(Expense expense);

    @Named("toExpenseDto")
    List<ExpenseDto> toExpenseDtos(List<Expense> expense);



    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "paidBy", source = "paidBy")
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "expenseSplits",ignore = true)
    @Mapping(target = "createdOn",source = "createdOn")
    @Named("toSimpleExpenseDto")
    ExpenseDto toSimplExpenseDto(Expense expense);

    @Named("toSimpleExpenseDto")
    List<ExpenseDto> toSimpleExpenseDtos(List<Expense> expense);

}
