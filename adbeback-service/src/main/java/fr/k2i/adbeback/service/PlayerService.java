package fr.k2i.adbeback.service;

import java.util.List;

import javax.jws.WebService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.k2i.adbeback.core.business.player.Player;

/**
 * Web Service interface so hierarchy of Generic Manager isn't carried through.
 */
@WebService
public interface PlayerService {
    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    Player getPlayer(String playerId);

    /**
     * Finds a user by their username.
     * @param username the user's username used to login
     * @return User a populated user object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     *         exception thrown when user not found
     */
    Player getPlayerByUsername(String username) throws UsernameNotFoundException;

    /**
     * Retrieves a list of all users.
     * @return List
     */
    List<Player> getPlayers();

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return updated user
     */
    Player savePlayer(Player player) throws UserExistsException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removePlayer(String playerId);
}
