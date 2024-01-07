package org.acme.mapper;

import org.acme.dto.DTOUser;
import org.acme.dto.DTOUserPathPartial;
import org.acme.entity.User;
import org.acme.util.DateUtil;
import org.mapstruct.*;

import java.util.List;

//@Mapper(componentModel = "cdi")
@Mapper(componentModel = "jakarta", imports = {DateUtil.class})
public interface UserMapper {

    @Mapping(target = "age", expression = "java(DateUtil.calculateAge(entity))")
    DTOUser toDTOUser(User entity);
    List<DTOUser> toDTOUserList(List<User> entities);

    @InheritInverseConfiguration(name = "toDTOUser")
    User toUser(DTOUser dto);

    void updateUserFromDTOUser(DTOUser dto, @MappingTarget User entity);

    void updateDTOUserFromUser(User entity, @MappingTarget DTOUser dto);

    void copyUser(User entityNEW, @MappingTarget User entityOLD);


    /*************************************************/
    /**********     DTOUserPatchPartial     **********/
    /*************************************************/

    DTOUserPathPartial toDTOUserPatchPartial(User entity);
    @InheritInverseConfiguration(name = "toDTOUserPatchPartial")
    User toUser(DTOUserPathPartial dtoPartial);

    void updateUserFromDTOUserPatchPartial(DTOUserPathPartial dtoPartial, @MappingTarget User entity);

}
