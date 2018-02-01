package pe.edu.utp.javawebregionlist.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionsEntity  extends BaseEntity{

    private static String DEFAULT_SQL = "SELECT * FROM hr.regions";

    private List<Region> findByCriteria(String sql)  {
        List<Region> regions = new ArrayList<>();
        if (getConnection() != null) {
            regions = new ArrayList<>();

            ResultSet resultSet = null;
            try {
                resultSet = getConnection()
                        .createStatement()
                        .executeQuery(sql);
                while (resultSet.next()) {
                    Region region = new Region()
                            .setId(resultSet.getInt("region_id"))
                            .setName(resultSet.getString("region_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        return regions;
    }
    //    Find All Method
    public List<Region> findAll(){
        return findByCriteria(DEFAULT_SQL);
    }
    //    Find by Id Method
    public Region findById(int id){
        List<Region> regions = findByCriteria(
                DEFAULT_SQL +
                        "WHERE region_id = " +
                        String.valueOf(id)
        );
        return (regions != null ? regions.get(0) : null);
    }
    //    Find by Name Method
    public Region findByName(String name){
        List<Region> regions = findByCriteria(
                DEFAULT_SQL +
                        "WHERE region_name = " +
                        name
        );
        return (regions != null ? regions.get(0) : null);
    }
    //    Find MaxId
    private int getMaxId(){
        String sql = "SELECT MAX(id) AS max_id FROM regions";
        if (getConnection() != null){
            try {
                ResultSet resultSet = getConnection()
                        .createStatement()
                        .executeQuery(sql);
                return resultSet.next() ?
                        resultSet.getInt("max_id") : 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    //    General Method to executeUpdate
    private int updateByCriteria(String sql){
        if (getConnection() != null){
            try {
                return getConnection()
                        .createStatement()
                        .executeUpdate(sql);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    //    Create region
    public Region create(String name){
        if (findByName(name) == null){
            if (getConnection() != null){
                String sql = "INSERT INTO regions(region_id, region_name) " +
                        "VALUES(" + String.valueOf(getMaxId() + 1) + ", '" +
                        name + "')";
                int results = updateByCriteria(sql);
                if (results > 0){
                    Region region = new Region(getMaxId(), name);
                    return region;
                }
            }
        }
        return null;
    }
    //    Delete by Id
    public boolean delete(int id){
        return updateByCriteria("DELETE FROM regions WHERE region_id = " +
                String.valueOf(id)) > 0;
    }
    //    Delete by Name
    public boolean delete(String name){
        return updateByCriteria("DELETE FROM regions WHERE region_name = '" +
                name + "'") > 0;
    }
    //    Update by Region Object
    public boolean update(Region region){
        return updateByCriteria("UPDATE regions " +
                "SET region_name = '" + region.getName() + "' " +
                "WHERE region_id = "+ String.valueOf(region.getId())) > 0;
    }
}
