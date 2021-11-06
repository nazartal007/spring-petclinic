package com.petclinic;

import com.petclinic.data.Owner;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerManager implements IOwnerManager {

	DataSource dataSource = DataSourceProvider.INSTANCE.getDataSource();

	//		INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES
//('art','naz', 'sdfsd', 'asdf')

	@Override
	public int createOwner(Owner owner) {


		try (Connection connection = dataSource.getConnection();
			 PreparedStatement ps = connection.prepareStatement(
			 	"INSERT INTO owners (first_name, last_name, address, city, telephone)" +
				 "VALUES (?,?, ?, ?, ?)",
				 Statement.RETURN_GENERATED_KEYS)){
			ps.setString(1, owner.getFirstName());
			ps.setString(2, owner.getLastName());
			ps.setString(3, owner.getAddress());
			ps.setString(4, owner.getCity());
			ps.setString(5, owner.getTelephone());
			ps.executeUpdate();

			ResultSet generatedKeys = ps.getGeneratedKeys();

			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void deleteOwnerById(int ownerId) {
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement ps = connection.prepareStatement(
				 "DELETE FROM owners WHERE id = ?")) {
			ps.setInt(1, ownerId);
			ps.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public List<Owner> findOwnerByFullName(String firstName, String lastName) {
		List<Owner> owners = new ArrayList<>();

		try (Connection connection = dataSource.getConnection();
			 PreparedStatement ps = connection.prepareStatement(
				 "SELECT * FROM owners WHERE first_name = ? AND last_name = ?")) {
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				owners.add(Owner.builder()
					.firstName(resultSet.getString("first_name"))
					.lastName(resultSet.getString("last_name"))
					.address(resultSet.getString("address"))
					.city(resultSet.getString("city"))
					.telephone(resultSet.getString("telephone"))
					.build());
			}
			return owners;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.EMPTY_LIST;
	}
}
