package im.tox.upsourcebot.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import java.util.List;
import java.util.Optional;

import im.tox.upsourcebot.core.UpsourceInstance;

/**
 * DAO interface for Upsource instances
 */
@RegisterMapper(UpsourceInstance.UpsourceInstanceMapper.class)
public interface UpsourceInstanceDAO {

  /**
   * Returns an upsource instance with the specified ID.
   *
   * @param id ID to select
   */
  @SqlQuery("select id, url, name from upsourceinstance where id = :id")
  @SingleValueResult
  Optional<UpsourceInstance> findById(@Bind("id") long id);

  /**
   * Returns all upsource instances.
   */
  @SqlQuery("select id, url, name from upsourceinstance")
  List<UpsourceInstance> getAll();

  /**
   * Returns an inclusive range of Upsource instances by ID.
   *
   * @param low  the lowest ID to select
   * @param high the highest ID to select
   */
  @SqlQuery("select id, url, name from upsourceinstance where id >= :low and id <= :high")
  List<UpsourceInstance> getInclusiveRange(@Bind("low") long low, @Bind("high") long high);

  /**
   * Insert a new Upsource instance.
   *
   * @param name name for the instance
   * @param url  url for the instance
   * @return generated ID
   */
  @SqlUpdate("insert into upsourceinstance (name, url) values (:name, :url)")
  @GetGeneratedKeys
  long insert(@Bind("name") String name, @Bind("url") String url);

}
