package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Utilisateur;
import com.eburtis.assurance.exception.AssuranceException;
import com.eburtis.assurance.repository.UtilisateurRepository;
import com.eburtis.assurance.security.JwtTokenUtils;
import com.eburtis.assurance.enums.StatutUtilisateur;
import com.eburtis.assurance.presentation.dto.auth.AuthDto;
import com.eburtis.assurance.presentation.dto.auth.TokenDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class SecurityService implements UserDetailsService {

    private final JwtTokenUtils jwtTokenUtils;
    private final UtilisateurRepository utilisateurRepository;

    public SecurityService(JwtTokenUtils jwtTokenUtils, UtilisateurRepository utilisateurRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère un utilisateur à partir de son username.
     *
     * @param username le username de l'utilisateur.
     * @return L'utilisateur.
     * @throws UsernameNotFoundException exception levée lorsqu'aucun utilisateur ne correspond à ce username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.rechercherParUsername(username.trim())
                .map(Utilisateur::buildUser)
                .orElseThrow(() -> AssuranceException.notFound("UTILISATEUR_INCONNU",
                        "Aucun utilisateur trouvé avec le login " + username + "."));
    }

    /**
     * Récupère un utilisateur à partir de son username et de son password.
     *
     * @param username le username de l'utilisateur.
     * @param password le password de l'utilisateur.
     * @return L'utilisateur.
     * @throws UsernameNotFoundException exception levée lorsqu'aucun utilisateur ne correspond à ce username.
     */
    private Utilisateur rechercherUtilisateurParUsernameEtPassword(String username, String password) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.rechercherParUsername(username)
                .orElseThrow(() -> AssuranceException.notFound("UTILISATEUR_INCONNU",
                        "Aucun utilisateur trouvé avec le login " + username + "."));
        if (SecurityService.comparerPassword(password, utilisateur.getPassword())) {
                return utilisateur;
        }
        else {
            throw AssuranceException.badRequest("MOT_DE_PASSE_INCORRECT", "Le mot de passe saisi est incorrect.");
        }
    }

    /**
     * Authentifie l'utilisateur.
     *
     * @param authDto l'utilisateur.
     * @return le token JWT de l'utilisateur authentifié.
     */
    @Transactional
    public TokenDto authentifier(AuthDto authDto) {
        Utilisateur utilisateur = rechercherUtilisateurParUsernameEtPassword(authDto.getUsername(), authDto.getPassword());
        if (utilisateur.getStatut().equals(StatutUtilisateur.INACTIF)) {
            throw AssuranceException.forbidden("UTILISATEUR_INACTIF",
                    "Cet utilisateur est inactif. Contactez votre administrateur.");
        }

        SecurityContextHolder.clearContext();
        String accessToken = jwtTokenUtils.generateToken(utilisateur);

        return new TokenDto(accessToken, utilisateur.isMustChangePassword());
    }

    /**
     * Compare les mots de passe.
     *
     * @param password le mot de passe.
     * @param encodePassword le mot de passe crypté.
     * @return le mot de passe crypté.
     */
    public static boolean comparerPassword(String password, String encodePassword) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
        return bCryptPasswordEncoder.matches(password, encodePassword);
    }

    /**
     * Crypte le mot de passe de l'utilisateur.
     *
     * @param password le mot de passe.
     * @return le mot de passe crypté.
     */
    public static String crypterPassword(String password) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }
}
