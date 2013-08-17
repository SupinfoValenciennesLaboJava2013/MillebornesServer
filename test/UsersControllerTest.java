import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.cookie;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.route;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import java.util.HashMap;

import org.junit.Test;

import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.test.FakeRequest;
import controllers.routes;


@SuppressWarnings("serial")
public class UsersControllerTest {
	
	private Cookie cookies;
	
	@Test
	public void routes() {
		// Test des urls et des méthodes
		assertThat(routes.Users.register().url()).isEqualTo("/users/register");
		assertThat(routes.Users.register().method()).isEqualTo("POST");
		assertThat(routes.Users.login().url()).isEqualTo("/users/login");
		assertThat(routes.Users.login().method()).isEqualTo("POST");
		assertThat(routes.Users.logout().url()).isEqualTo("/users/logout");
		assertThat(routes.Users.logout().method()).isEqualTo("POST");
		assertThat(routes.Users.info().url()).isEqualTo("/users/info");
		assertThat(routes.Users.info().method()).isEqualTo("GET");
		assertThat(routes.Users.whoami().url()).isEqualTo("/users/whoami");
		assertThat(routes.Users.whoami().method()).isEqualTo("GET");
	}
	
    @Test
    public void notLoggedIn() {
    	running(fakeApplication(), new Runnable() {
    		public void run() {
    			// Whoami doit retourner une erreur si on est pas connecté
    	        assertThat(status(route(Whoami()))).isNotEqualTo(OK);
    		}
    	});
    }
    
    @Test
    public void register() {
    	running(fakeApplication(), new Runnable() {
    		public void run() {
    	        // Test de l'inscription
    	        Result result = route(Register("tata2", "pwd"));
    	        assertThat(status(result)).isEqualTo(OK);
    	        
    	        // L'inscription doit échouer si le username est pris
    	        result = route(Register("tata2", "pwd2"));
    	        assertThat(status(result)).isNotEqualTo(OK);
    		}
    	});
    }
    
    @Test
    public void login() {
    	running(fakeApplication(), new Runnable() {
    		public void run() {
    	        // Test de la connexion
    			route(Register("tata", "pwd"));
    	        assertThat(status(route(Login("tata", "pwd")))).isEqualTo(OK);
    		}
    	});
    }
    
    @Test
    public void logout() {
    	running(fakeApplication(), new Runnable() {
    		public void run() {
    	        // Test de la déconnexion
    			cookies = cookie("PLAY_SESSION", route(Register("tata", "pwd")));
    			route(Login("tata", "pwd"));
    			assertThat(cookies).isNotNull();
    			//route(Logout());
    	        //assertThat(status(route(Logout()))).isEqualTo(OK);
    		}
    	});
    }

	// Methodes pour générer les fausses requetes sur les actions (avec paramètres)
    
	public FakeRequest Register(final String username, final String password) {
		return new FakeRequest(routes.Users.register().method(), routes.Users.register().url())
			.withFormUrlEncodedBody(new HashMap<String, String>() {{
				put("username", username);
				put("password", password);
			}});
	}
    
	public FakeRequest Login(final String username, final String password) {
		return new FakeRequest(routes.Users.login().method(), routes.Users.login().url())
			.withFormUrlEncodedBody(new HashMap<String, String>() {{
				put("username", username);
				put("password", password);
			}});
	}
    
	public FakeRequest Logout() {
		return new FakeRequest(routes.Users.logout().method(), routes.Users.logout().url()).withCookies(cookies);
	}
	
	public FakeRequest Whoami() {
		return new FakeRequest(routes.Users.whoami().method(), routes.Users.whoami().url());
	}
	
}
