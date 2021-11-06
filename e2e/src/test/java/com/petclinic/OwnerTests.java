package com.petclinic;

import com.petclinic.data.Owner;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class OwnerTests {
	private IOwnerManager om = new SpringOwnerManager();
	private int ownerId;

	@BeforeEach
	public void createOwner(){
		// todo надо сделать метод который проверяет наличие юзера и если есть, то удалять его, но это уже на спринге

		ownerId = om.createOwner(
			Owner.builder()
				.firstName("art")
				.lastName("naz")
				.address("kemerov")
				.city("kemerovo")
				.telephone("123456")
				.build()
		);
	}

	@AfterEach
	public void deleteOwner() {
		om.deleteOwnerById(ownerId);
	}


	@RepeatedTest(2)
	public void findOwner() {
		open("http://localhost:8080/owners/find");
		$("#lastName").setValue("Franklin");
		$("button[type='submit']").click();
		$("table.table").should(visible);
		$$("tr").find(text("Name")).should(text("George Franklin"));
	}

	@Test
	public void addOwner() {
		open("http://localhost:8080/owners/new");
		$("#firstName").setValue("firstName");
		$("#lastName").setValue("lastName");
		$("#address").setValue("address");
		$("#city").setValue("city");
		$("#telephone").setValue("123456");
		$("button[type='submit']").click();

		Owner actualOwner = om.findOwnerByFullName("firstName", "lastName").get(0);

		Assertions.assertEquals(actualOwner.getAddress(), "address");
		Assertions.assertEquals(actualOwner.getCity(), "city");
		Assertions.assertEquals(actualOwner.getTelephone(), "123456");
	}
}
