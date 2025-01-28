package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class FriendshipDaoJdbc implements FriendshipDao {

    private static  final Config CFG = Config.getInstance();
    @Override
    public void createFriendshipPending(UserEntity requester, UserEntity addressee) {
        try(PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO public.friendship(requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?);"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, "PENDING");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFriendshipAccepted(UserEntity requester, UserEntity addressee) {
        try(PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO public.friendship(requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?);"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, "ACCEPTED");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            ps.executeUpdate();
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, "ACCEPTED");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
