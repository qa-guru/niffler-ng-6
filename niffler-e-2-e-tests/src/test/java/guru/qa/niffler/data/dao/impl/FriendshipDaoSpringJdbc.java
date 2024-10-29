package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class FriendshipDaoSpringJdbc implements FriendshipDao {

    private static  final Config CFG = Config.getInstance();
    @Override
    public void createFriendshipPending(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update( con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO public.friendship(requester_id, addressee_id, status, created_date) " +
                            "VALUES (?, ?, ?, ?);"
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, "PENDING");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            return ps;
        });
    }

    @Override
    public void createFriendshipAccepted(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update( con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO public.friendship(requester_id, addressee_id, status, created_date) " +
                            "VALUES (?, ?, ?, ?);"
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, "PENDING");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            return ps;
        });
        jdbcTemplate.update( con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO public.friendship(requester_id, addressee_id, status, created_date) " +
                            "VALUES (?, ?, ?, ?);"
            );
            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, "PENDING");
            ps.setDate(4, new Date(new java.util.Date().getTime()));
            return ps;
        });
    }
}
