package za.co.wethinkcode.gadgethomeserver.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import za.co.wethinkcode.gadgethomeserver.models.database.DeviceToken;
import za.co.wethinkcode.gadgethomeserver.models.domain.DeviceTokenDto;

@Mapper( componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DeviceTokenMapper extends EntityMapper<DeviceTokenDto, DeviceToken>{
}
