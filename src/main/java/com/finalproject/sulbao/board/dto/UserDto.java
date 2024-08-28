package com.finalproject.sulbao.board.dto;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.vo.MemberImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserDto {

    public static final String DEFAULT_PROFILE_IMAGE_FILE_URL = "https://kr.object.ncloudstorage.com/sulbao-file/profile/default-profile.png";
    private Long id;
    private String profileImageFileUrl;
    private String profileName;
    private RoleType roleType;
    private String userId;

    public static UserDto toUserDto(Login login) {
        MemberImage memberImage = login.getMemberInfo().getMemberImage();
        String profileImageFileUrl = DEFAULT_PROFILE_IMAGE_FILE_URL;
        if (memberImage != null) {
            profileImageFileUrl = memberImage.getSaveImgUrl();
        }

        return UserDto.builder()
                .id(login.getUserNo())
                .profileImageFileUrl(profileImageFileUrl)
                .profileName(login.getMemberInfo().getProfileName())
                .roleType(login.getUserRole())
                .userId(login.getUserId())
                .build();
    }

    public static UserDto getAnonymous() {
        return UserDto.builder()
                .id(0L)
                .profileImageFileUrl(null)
                .profileName(null)
                .roleType(RoleType.MEMBER)
                .build();
    }

}
