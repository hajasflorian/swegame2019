package map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class MapGenerationTest {
	
	//MUST BE STATIC!
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		//executed once, before all tests in this class
		//use to prepare stuff and dependencies
		
		System.out.println("setUpBeforeClass");
	}

	//MUST BE STATIC!
	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		//executed once, after all tests in this class
		//use to clean up stuff and dependencies
		
		System.out.println("tearDownAfterClass");
	}

	@BeforeEach
	public void setUp() throws Exception {
	
		System.out.println("setUp");
	}

	@AfterEach
	public void tearDown() throws Exception {
		//executed after each test in this class
		//use to clean up stuff and dependencies
		
		System.out.println("tearDown");
	}

	@Test
	public void testWhenCreatingMap_ShouldNotHasIslands() {
		MapGenerator mapgenerator = new MapGenerator();
		//when
		HashMap<Point, TerrainType> map = mapgenerator.createMap();
		boolean hasMapIslands = mapgenerator.hasIslands(map);
		//then
		assertThat(hasMapIslands, is(equalTo(false)));
	}

}