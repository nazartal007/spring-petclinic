package com.petclinic;

import com.petclinic.data.Owner;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;

public class SpringOwnerManager implements IOwnerManager{

	private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.getDataSource());

	@Override
	public int createOwner(Owner owner) {
		return new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("owners")
			.usingGeneratedKeyColumns("id")
			.executeAndReturnKey(new HashMap<String,String>(){{
				put("first_name",owner.getFirstName());
				put("last_name",owner.getLastName());
				put("address",owner.getAddress());
				put("city",owner.getCity());
				put("telephone",owner.getTelephone());
			}})
			.intValue();
	}

	@Override
	public void deleteOwnerById(int ownerId) {
		jdbcTemplate.update("DELETE FROM owners WHERE id = ?", ownerId);
	}

	@Override
	public List<Owner> findOwnerByFullName(String firstName, String lastName) {
		List<Owner> owners = jdbcTemplate.query("SELECT * FROM owners WHERE first_name = ? AND last_name = ?",
			(rs, rowNum) -> new Owner(
				rs.getInt("id"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("address"),
				rs.getString("city"),
				rs.getString("telephone")
				), firstName, lastName);

		return owners;
	}
}
