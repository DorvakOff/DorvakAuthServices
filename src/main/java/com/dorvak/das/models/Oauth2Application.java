package com.dorvak.das.models;

import com.dorvak.das.DorvakAuthServices;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "oauth2_applications")
public class Oauth2Application {

    @Id
    @GeneratedValue(generator = "snowflake_generator")
    @GenericGenerator(name = "snowflake_generator", strategy = "com.dorvak.das.appconfig.SnowflakeGenerator")
    private String clientId;

    private String name;
    private String description;
    @ElementCollection(fetch =  FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "redirect_uri")
    private Set<String> redirectUris = new HashSet<>();
    private String icon;
    private UUID owner;

    public Oauth2Application() {
    }

    public Oauth2Application(String name, String description, UUID owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public void addRedirectUri(String redirectUri) {
        this.redirectUris.add(redirectUri);
    }

    public void save() {
        DorvakAuthServices.getInstance().getOauth2ApplicationRepository().save(this);
    }
}
