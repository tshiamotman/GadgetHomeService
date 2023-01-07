package za.co.wethinkcode.gadgethomeserver.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.domain.ChatDto;

@Mapper( componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ChatMapper extends EntityMapper<ChatDto, Chat>{
}
