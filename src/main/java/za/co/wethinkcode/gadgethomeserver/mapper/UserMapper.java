package za.co.wethinkcode.gadgethomeserver.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;

@Mapper( componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper extends EntityMapper<UserDto, User>{
}
