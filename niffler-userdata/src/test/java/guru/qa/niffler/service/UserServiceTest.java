package guru.qa.niffler.service;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.FriendshipEntity;
import guru.qa.niffler.data.FriendshipStatus;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.projection.UserWithStatus;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.ex.NotFoundException;
import guru.qa.niffler.ex.SameUsernameException;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.UserJsonBulk;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.model.FriendState.FRIEND;
import static guru.qa.niffler.model.FriendState.INVITE_SENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService testedObject;

    private final UUID mainTestUserUuid = UUID.randomUUID();
    private final String mainTestUserName = "dima";
    private UserEntity mainTestUser;

    private final UUID secondTestUserUuid = UUID.randomUUID();
    private final String secondTestUserName = "barsik";
    private UserEntity secondTestUser;

    private final UUID thirdTestUserUuid = UUID.randomUUID();
    private final String thirdTestUserName = "emma";
    private UserEntity thirdTestUser;


    private final String notExistingUser = "not_existing_user";


    @BeforeEach
    void init() {
        mainTestUser = new UserEntity();
        mainTestUser.setId(mainTestUserUuid);
        mainTestUser.setUsername(mainTestUserName);
        mainTestUser.setCurrency(CurrencyValues.RUB);

        secondTestUser = new UserEntity();
        secondTestUser.setId(secondTestUserUuid);
        secondTestUser.setUsername(secondTestUserName);
        secondTestUser.setCurrency(CurrencyValues.RUB);

        thirdTestUser = new UserEntity();
        thirdTestUser.setId(thirdTestUserUuid);
        thirdTestUser.setUsername(thirdTestUserName);
        thirdTestUser.setCurrency(CurrencyValues.RUB);
    }


    @ValueSource(strings = {
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAACwVBMVEUanW4am2wBY0QYmWsMfFYBZEQAAQBHcEz+" +
                    "/v4AAAAZm20anW4bnW4IdFAanW4anW4BZUQbnm8bnm8anW4anG4VflganW4anW4Jd1Ibnm8EbEoDaUcbnW4anW4anW4bnm8anW4anW4" +
                    "anG4BZUQWlGcHKR0anG0anW4FbksanG4anW4FbEobnm4anW4anW4anW4anW4anW4Zm2wanG0ZmmwZm20BY0MZmmwanW4anG4anG0cnm9B" +
                    "roj0+vj9/v4hoXIAYkMfoHEMfFYLe1QWlGgTjWIQh14XlWgNf1gNf1gAYUIAYkIAYUIAYUIAYUIKeFMAYUIEakgAYUISimAAYUIAYEEAYUE" +
                    "AYUIAYUIAYUIBCwcanW4MSjQHLB8BBwUJOCcNTzcSbUwVe1YanW4AAwIPWj8Sakvj8+0LQC0ZmmwAY0MAY0MAYkNdupglonVovp9RtJFbuZg1" +
                    "qX8Zm2yAyK7f8er3+/p9x6yk2MUDaUi038+Fy7IHck6w3c0KeFMGb0wEakllvZ4IdVAGcE0Le1UKeVMbnm8AAAD///8AYUIanW4Zm20amGsCEAs" +
                    "GJhoMfFYZmmwOVj0EHBQYj2QBBgQSbEwYjGIam2wUd1MYkmYCDQkFHhUOVDsJdVEJNSUZlWgSbU0Zk2cVkmYABANqv6Hs9vKr28pTtpLn9fBeupn+/" +
                    "v46q4Mdn3D6/PsDaUcPhFwBZEQIc08AYkIYmWsVfliU0bsAAQALQS0ZlWkOUjkYjmMUeFSb1MB5xqoBCQYDFg8KPSsHKx4Zl2oYkGV3xakanG4NUDgcnnC" +
                    "OzrcIMiMzqH5IsYv1+vmHzLNauJbF5tq84tRUtpPX7eWg1sJDr4gOgVn6/fyCyrArpXlwwqWm2cfa7+goo3fB5Ng9rIUjoXQhoHOe1sLT7ONLso0OgVrt9/P9/v2" +
                    "+49XI59zW7eUKeVOYug0QAAAAhnRSTlP+/q/+/gX+AP7+/vYU/QO9kAb7sv7+EcH+helU03EZM/0BIQj+/vv3E+2o/rBEFZlayYYr4Aj9RPrrlv7+/v7+VP71Bf7Zs" +
                    "PXhfOkTkvUd/ob+OP58/uHz2bD+0P7+/v7+/v7P/v7+/v66r5CQ/v7+/v7+uv7+/v7+kv7+Hf44hpL+HYY4/hahmy4AAAMTSURBVEjHY2BHBqbmRsUmagwMbVCkZpJp" +
                    "ZG6KooQBiW1sZQlXCkdtDJZWxlg1KGlZMDBg08DQZqGlhKGBT5a7DQ/gluVD1SAmDDEMuw0gJCyGrEEumoGQBoZkOYQGHpk2IoAMD0yDID/cMDw2tDHwC0I1SDIQp4FBEq" +
                    "JBXgirC7oXTpvcgyokJA/SoCjehmkDV+K8XrZ2zri+iYeR7RNXBGrQVcDUMGMSZzsnBO2bw4jQoKAL1KCD4ZiuOdvakUDfTISUDjuDnjoDmg1dCRCzo/aDXAVkzp4PV6Kux2DA" +
                    "gK5hARtQFdsBUeY2kWkTZ4H0xiO8YcCgj+6gQ71AZ8zaDeXNPNjePmkLQlafwRDdhlSgmVNE4YI9fTE9SAFlyKCJbsNsoAU7kfjTUWQ1GRjQbJicAgz+Hdhjuo2BAT0VMLTNAPo4F" +
                    "ipivxybBjQwFeii7RDm40drV2NEEoYNiUAbMsDc6mccHA8JOykJqEEVzG3s4OBY4UjQSfMXt7cvXghirXna0bHqHkEnMagC4+EYWOQ5B0dHLkEnMaS1c87bBRZZBtSwhKCT2o6eXNAFYV" +
                    "3o6OhYSdhJcGS9lINjlQ2GDd04NawEhtJS9FDiYnBHtm/1OQT7/kugi26hu8idwRWhfU25re1yGPdBLUcHh90V9IhzZXBD8IChwnGmxB7MtnkBDCKOCoy05MbgibDu4lqgIzpWXD5y8+zdayD" +
                    "mpTsYYejJUOeC8HR+GdBUBLpdhJG8XVoYnD2QQqmgFElD1Q3M/ODhzMBez4wUrHnX0yGq7c4XWmNmIOZmYLlU44TiyOOn927dc2LJKazFp1MTqGytXERsYbyoAVwYa0hNaCMKTJDSgBT30rwixN" +
                    "ggwisNq4EEJLoIa+iSEIBXWaxMc7sJuad7LhMrolJUztJWwW+DijaTMnK1y5pj9oQLtwauq2bZrGgtAQevdetxuKt7/TovB8ymg7eP7wb/4M3oNmwO9t/g6+ONtXHiFxAWuilw4ysWln4oat0YuCk0" +
                    "LMAPR2sG6JWgkMiI8E44CI+IDAliRVECAE4WhZg/rX3CAAAAAElFTkSuQmCC",
            ""
    })
    @ParameterizedTest
    void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(mainTestUserName)))
                .thenReturn(Optional.of(mainTestUser));

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(answer -> answer.getArguments()[0]);

        testedObject = new UserService(userRepository);

        final String photoForTest = photo.isEmpty() ? null : photo;

        final UserJson toBeUpdated = new UserJson(
                null,
                mainTestUserName,
                "Test",
                "TestSurname",
                "Test TestSurname",
                CurrencyValues.USD,
                photoForTest,
                null,
                null
        );
        final UserJson result = testedObject.update(toBeUpdated);
        assertEquals(mainTestUserUuid, result.id());
        assertEquals("Test TestSurname", result.fullname());
        assertEquals(CurrencyValues.USD, result.currency());
        assertEquals(photoForTest, result.photo());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
        when(userRepository.findByUsername(eq(notExistingUser)))
                .thenReturn(Optional.empty());

        testedObject = new UserService(userRepository);

        final NotFoundException exception = assertThrows(NotFoundException.class,
                () -> testedObject.getRequiredUser(notExistingUser));
        assertEquals(
                "Can`t find user by username: '" + notExistingUser + "'",
                exception.getMessage()
        );
    }

    @Test
    void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
        when(userRepository.findByUsernameNot(eq(mainTestUserName)))
                .thenReturn(getMockUsersMappingFromDb());

        testedObject = new UserService(userRepository);

        final List<UserJsonBulk> users = testedObject.allUsers(mainTestUserName, null);
        assertEquals(2, users.size());
        final UserJsonBulk invitation = users.stream()
                .filter(u -> u.friendState() == INVITE_SENT)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));

        final UserJsonBulk friend = users.stream()
                .filter(u -> u.friendState() == null)
                .findFirst()
                .orElseThrow(() -> new AssertionError("user without status not found"));


        assertEquals(secondTestUserName, invitation.username());
        assertEquals(thirdTestUserName, friend.username());
    }


    @Test
    void smallPhoto(@Mock UserRepository userRepository) {
        List<UserWithStatus> mockUsers = List.of(
                new UserWithStatus(UUID.randomUUID(), "user1", CurrencyValues.RUB, "User One", "smallPhoto".getBytes(), FriendshipStatus.PENDING),
                new UserWithStatus(UUID.randomUUID(), "user2", CurrencyValues.RUB, "User Two", null, FriendshipStatus.ACCEPTED)
        );

        when(userRepository.findByUsernameNot(eq(mainTestUserName))).thenReturn(mockUsers);
        UserService userService = new UserService(userRepository);

        List<UserJsonBulk> result = userService.allUsers(mainTestUserName, null);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("smallPhoto", result.get(0).photoSmall());
        Assertions.assertNull(result.get(1).photoSmall());

        verify(userRepository, times(1)).findByUsernameNot(eq(mainTestUserName));
    }


    @Test
    void getCurrentUserShouldReturnUserJsonWhenWithoutHimInBd(@Mock UserRepository userRepository) {
        UserService userService = new UserService(userRepository);
        final String username = "Ignat";
        Mockito.when(userRepository.findByUsername(eq(username)))
                .thenReturn(Optional.empty());

        UserJson result = userService.getCurrentUser(username);

        Assertions.assertEquals(result.username(), username);
    }


    @Test
    void getCurrentUserShouldReturnUserJsonFromBd(@Mock UserRepository userRepository) {
        UserService userService = new UserService(userRepository);
        final String username = "Ignat";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setId(UUID.randomUUID());
        userEntity.setCurrency(CurrencyValues.RUB);
        userEntity.setFullname("Petrov Ignat");

        Mockito.when(userRepository.findByUsername(eq(username)))
                .thenReturn(Optional.of(userEntity));

        UserJson result = userService.getCurrentUser(username);

        Assertions.assertEquals(result.username(), username);
        Assertions.assertEquals(result.fullname(), "Petrov Ignat");
    }


    @Test
    void allUserReturnPageCallMethodWithSearchQuery(@Mock UserRepository userRepository) {
        String username = "Ignat";
        String searchQuery = "some text";
        List<UserWithStatus> userWithStatuses = new ArrayList<>();
        UserService userService = new UserService(userRepository);
        userService.allUsers(username, searchQuery);
        verify(userRepository, times(1)).findByUsernameNot(username, searchQuery);
    }

    @Test
    void allUserReturnPageCallMethodWithoutSearchQuery(@Mock UserRepository userRepository) {
        String username = "Ignat";
        List<UserWithStatus> userWithStatuses = new ArrayList<>();
        UserService userService = new UserService(userRepository);
        userService.allUsers(username, null);
        verify(userRepository, times(1)).findByUsernameNot(username);
    }

    @Test
    void allUserPageableReturnPageCallMethodWithSearchQuery(@Mock UserRepository userRepository) {
        String username = "Ignat";
        String searchQuery = "some text";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        Page<UserWithStatus> userWithStatusesPage = new PageImpl<>(userWithStatusList);
        Mockito.when(userRepository.findByUsernameNot(username, searchQuery, PageRequest.of(1, 1)))
                .thenReturn(userWithStatusesPage);
        UserService userService = new UserService(userRepository);
        userService.allUsers(username, PageRequest.of(1, 1), searchQuery);
        verify(userRepository, atMostOnce()).findByUsernameNot(username, searchQuery, PageRequest.of(1, 1));
    }

    @Test
    void allUserPageableReturnPageCallMethodWithoutSearchQuery(@Mock UserRepository userRepository) {
        String username = "Ignat";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        Page<UserWithStatus> userWithStatusesPage = new PageImpl<>(userWithStatusList);
        Mockito.when(userRepository.findByUsernameNot(username, PageRequest.of(1, 1)))
                .thenReturn(userWithStatusesPage);
        UserService userService = new UserService(userRepository);
        userService.allUsers(username, PageRequest.of(1, 1), null);
        verify(userRepository, atMostOnce()).findByUsernameNot(username, PageRequest.of(1, 1));
    }

    @Test
    void friendsReturnPageCallMethodWithSearchQuery(@Mock UserRepository userRepository) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Ignat");
        String searchQuery = "some text";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        Mockito.when(userRepository.findByUsername(userEntity.getUsername()))
                .thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findFriends(eq(userEntity), eq(searchQuery)))
                .thenReturn(userWithStatusList);
        UserService userService = new UserService(userRepository);
        userService.friends(userEntity.getUsername(), searchQuery);
        verify(userRepository, times(1)).findFriends(userEntity, searchQuery);
    }

    @Test
    void friendsUserReturnPageCallMethodWithoutSearchQuery(@Mock UserRepository userRepository) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Ignat");
        String searchQuery = "some text";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        Page<UserWithStatus> userWithStatusesPage = new PageImpl<>(userWithStatusList);
        Mockito.when(userRepository.findByUsername(userEntity.getUsername()))
                .thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findFriends(eq(userEntity), eq(PageRequest.of(1, 1))))
                .thenReturn(userWithStatusesPage);
        UserService userService = new UserService(userRepository);
        userService.friends(userEntity.getUsername(), PageRequest.of(1, 1), null);
        verify(userRepository, times(1)).findFriends(userEntity, PageRequest.of(1, 1));
    }

    @Test
    void friendsPageableReturnPageCallMethodWithSearchQuery(@Mock UserRepository userRepository) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("Ignat");
        String searchQuery = "some text";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        Page<UserWithStatus> userWithStatusesPage = new PageImpl<>(userWithStatusList);
        Mockito.when(userRepository.findByUsername(userEntity.getUsername()))
                .thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findFriends(eq(userEntity), eq(searchQuery), eq(PageRequest.of(1, 1))))
                .thenReturn(userWithStatusesPage);
        UserService userService = new UserService(userRepository);
        userService.friends(userEntity.getUsername(), PageRequest.of(1, 1), searchQuery);
        verify(userRepository, times(1)).findFriends(userEntity, searchQuery, PageRequest.of(1, 1));
    }

    @Test
    void friendPageableReturnPageCallMethodWithoutSearchQuery(@Mock UserRepository userRepository) {
        String username = "Ignat";
        List<UserWithStatus> userWithStatusList = new ArrayList<>();
        UserWithStatus userWithStatus = new UserWithStatus(
                null,
                null,
                null,
                null,
                null,
                null
        );
        userWithStatusList.add(userWithStatus);
        Page<UserWithStatus> userWithStatusesPage = new PageImpl<>(userWithStatusList);
        Mockito.when(userRepository.findByUsernameNot(username, PageRequest.of(1, 1)))
                .thenReturn(userWithStatusesPage);
        UserService userService = new UserService(userRepository);
        userService.allUsers(username, PageRequest.of(1, 1), null);
        verify(userRepository, atMostOnce()).findByUsernameNot(username, PageRequest.of(1, 1));
    }


    @Test
    void checkShouldNotCreateFriendshipFromSelfName(@Mock UserRepository userRepository) {
        final String username = "Ignat";
        UserService userService = new UserService(userRepository);
        SameUsernameException ex = Assertions.assertThrows(
                SameUsernameException.class,
                () -> userService.createFriendshipRequest(username, username)
        );
        Assertions.assertEquals(ex.getMessage(), "Can`t create friendship request for self user");
    }

    @Test
    void checkShouldCreateFriendship(@Mock UserRepository userRepository) {

        UserEntity currentUser = new UserEntity();
        currentUser.setUsername("Ignat");
        UserEntity targetUser = new UserEntity();
        targetUser.setUsername("Ivan");
        Mockito.when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));
        Mockito.when(userRepository.findByUsername(targetUser.getUsername()))
                .thenReturn(Optional.of(targetUser));
        UserService userService = new UserService(userRepository);
        UserJson userJson = userService.createFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());
        Assertions.assertEquals(userJson.friendState(), INVITE_SENT);
    }

    @Test
    void checkShouldAcceptFriendship(@Mock UserRepository userRepository) {
        UserEntity currentUser = new UserEntity();
        currentUser.setUsername("Ignat");
        UserEntity targetUser = new UserEntity();
        targetUser.setUsername("Ivan");
        List<FriendshipEntity> friendshipEntityList = new ArrayList<>();
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(targetUser);
        friendshipEntityList.add(friendshipEntity);
        currentUser.setFriendshipAddressees(friendshipEntityList);
        Mockito.when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));
        Mockito.when(userRepository.findByUsername(targetUser.getUsername()))
                .thenReturn(Optional.of(targetUser));

        UserService userService = new UserService(userRepository);
        UserJson userJson = userService.acceptFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());
        Assertions.assertEquals(userJson.friendState(), FRIEND);
    }

    @Test
    void checkShouldDeclineFriendship(@Mock UserRepository userRepository) {
        UserEntity currentUser = new UserEntity();
        currentUser.setUsername("Ignat");
        UserEntity targetUser = new UserEntity();
        targetUser.setUsername("Ivan");
        List<FriendshipEntity> friendshipEntityList = new ArrayList<>();
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(targetUser);
        friendshipEntityList.add(friendshipEntity);
        currentUser.setFriendshipAddressees(friendshipEntityList);
        List<FriendshipEntity> friendshipEntityTargetList = new ArrayList<>();
        FriendshipEntity friendshipEntityTarget = new FriendshipEntity();
        friendshipEntityTarget.setAddressee(currentUser);
        friendshipEntityTargetList.add(friendshipEntity);
        targetUser.setFriendshipAddressees(friendshipEntityList);

        Mockito.when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));
        Mockito.when(userRepository.findByUsername(targetUser.getUsername()))
                .thenReturn(Optional.of(targetUser));

        UserService userService = new UserService(userRepository);
        UserJson userJson = userService.declineFriendshipRequest(currentUser.getUsername(), targetUser.getUsername());
        Assertions.assertEquals(userJson.friendState(), null);
    }

    @Test
    void checkShouldRemoveFriend(@Mock UserRepository userRepository) {
        UserEntity currentUser = new UserEntity();
        currentUser.setUsername("Ignat");
        UserEntity targetUser = new UserEntity();
        targetUser.setUsername("Ivan");
        List<FriendshipEntity> friendshipEntityList = new ArrayList<>();
        FriendshipEntity friendshipEntity = new FriendshipEntity();
        friendshipEntity.setRequester(targetUser);
        friendshipEntity.setAddressee(targetUser);
        friendshipEntityList.add(friendshipEntity);
        currentUser.setFriendshipAddressees(friendshipEntityList);
        List<FriendshipEntity> friendshipEntityTargetList = new ArrayList<>();
        FriendshipEntity friendshipEntityTarget = new FriendshipEntity();
        friendshipEntityTarget.setAddressee(currentUser);
        friendshipEntityTarget.setRequester(currentUser);
        friendshipEntityTargetList.add(friendshipEntity);
        targetUser.setFriendshipAddressees(friendshipEntityList);

        Mockito.when(userRepository.findByUsername(currentUser.getUsername()))
                .thenReturn(Optional.of(currentUser));
        Mockito.when(userRepository.findByUsername(targetUser.getUsername()))
                .thenReturn(Optional.of(targetUser));

        UserService userService = new UserService(userRepository);
        userService.removeFriend(currentUser.getUsername(), targetUser.getUsername());

        verify(userRepository, atMostOnce()).save(currentUser);
        verify(userRepository, atMostOnce()).save(targetUser);
    }

    private List<UserWithStatus> getMockUsersMappingFromDb() {
        return List.of(
                new UserWithStatus(
                        secondTestUser.getId(),
                        secondTestUser.getUsername(),
                        secondTestUser.getCurrency(),
                        secondTestUser.getFullname(),
                        secondTestUser.getPhotoSmall(),
                        FriendshipStatus.PENDING
                ),
                new UserWithStatus(
                        thirdTestUser.getId(),
                        thirdTestUser.getUsername(),
                        thirdTestUser.getCurrency(),
                        thirdTestUser.getFullname(),
                        thirdTestUser.getPhotoSmall(),
                        FriendshipStatus.ACCEPTED
                )
        );
    }
}