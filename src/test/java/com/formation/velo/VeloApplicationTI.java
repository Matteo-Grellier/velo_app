package com.formation.velo;

import com.formation.velo.controllers.UserController;
import com.formation.velo.model.Stations;
import com.formation.velo.model.User;
import com.formation.velo.service.StationsService;
import com.formation.velo.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VeloApplication.class)
@Sql({"classpath:data.sql"})
@Profile(value = "test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class VeloApplicationTI {


    @Autowired
    private  UserService userService;

    @Autowired
    private  StationsService stationsService;


    private MockMvc mockMvc;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }



    @Test
    public void givenPersonsEntityRepository_whenSaveAndRetreiveEntity_thenOK() {

        //Given
        User user1 = User.builder().surname("Julie").name("Dupont").build();
        User user2 = User.builder().surname("Marie").name("Dalle").build();
        userService.save(user1);
        userService.save(user2);


        List<User> people = userService.findAll();
        assertNotNull(people);
        assertEquals(4, people.size());

    }




    @Test
    public void setTimeZone() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("senegal"));
        assertEquals(calendar.getTimeZone(), TimeZone.getTimeZone("GMT"));

    }


    @Test
    public void getAllUsersAPI() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Souley")))
                .andExpect(jsonPath("$[0].surname", Matchers.is("Gueye")));

        List<User> users2 = userService.findAll();
        assertEquals(users2.size(), 2);
    }

    @Test
    public void getUserByIdAPI() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                        .get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Souley")))
                .andExpect(jsonPath("$.surname", Matchers.is("Gueye")));
    }

    @Test
    public void deleteUserByIdAPI() throws Exception
    {

        List<User> users = userService.findAll();
        assertEquals(users.size(), 2);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/users/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", Matchers.is("deleted")));
        List<User> users2 = userService.findAll();
        assertEquals(users2.size(), 1);
    }

    @Test
    public void getAddUsersAPI() throws Exception
    {
        User user = User.builder().name("Dupont").surname("Marie").build();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users/add")
                        .param("name","Dupont")
                        .param("surname","Marie")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(jsonPath("$.name", Matchers.is("Dupont")))
                .andExpect(jsonPath("$.surname", Matchers.is("Marie")));

        Optional<User> newUserAdded = userService.findById(2);
        assertNotNull(newUserAdded);
        assertTrue(newUserAdded.isPresent());
        assertEquals(newUserAdded.get().getId().intValue(), 2);
    }

    @Test
    public void getDeleteObjectUsersAPI()
    {
        User user1 = User.builder().surname("Julie").name("Dupont").build();
        User user2 = User.builder().surname("Marie").name("Dalle").build();
        userService.save(user1);
        userService.save(user2);


        List<User> people = userService.findAll();
        assertNotNull(people);
        assertEquals(4, people.size());

        userService.delete(user1);
        userService.delete(user2);
        List<User> peopleAfterDelete = userService.findAll();
        assertEquals(2, peopleAfterDelete.size());
    }


    @Test
    public void saveGetStationsReturnStationSuccess() {
        Stations stations = Stations.builder().recordId("id").name("test station").build();
        stationsService.save(stations);

        Optional<Stations> optionalStations = stationsService.findByRecordId("id");
        assertNotNull(optionalStations);
        assertEquals("test station", optionalStations.get().getName());
    }
}
