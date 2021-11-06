package com.petclinic;

import com.petclinic.data.Owner;

import java.util.List;

public interface IOwnerManager {
	int createOwner(Owner owner);

	void deleteOwnerById(int ownerId);

	List<Owner> findOwnerByFullName(String firstName, String lastName);
}
