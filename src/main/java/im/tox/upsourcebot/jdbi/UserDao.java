package im.tox.upsourcebot.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

import im.tox.upsourcebot.core.User;

@RegisterMapper(User.UserMapper.class)
public interface UserDao {

  @SqlUpdate("insert into user (name, passwordHash) values (:name, :passwordHash)")
  @GetGeneratedKeys
  long insert(@Bind("name") String name, @Bind("passwordHash") String passwordHash);

  @SqlUpdate("delete from user where user.id = :id")
  int delete(@Bind("id") long id);

  @SqlUpdate("update user set passwordHash = :passwordHash where id = :id")
  int updatePassword(@Bind("passwordHash") String passwordHash, @Bind("id") long id);

  @SqlQuery("select id, name, passwordHash from user where id = :id")
  User findById(@Bind("id") long id);

  @SqlQuery("select id, name, passwordHash from user where name = :name")
  User findByName(@Bind("name") String name);

  @SqlQuery("select id, name, passwordHash from user")
  List<User> getAll();

}
