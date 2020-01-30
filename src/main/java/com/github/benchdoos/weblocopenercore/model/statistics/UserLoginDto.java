package com.github.benchdoos.weblocopenercore.model.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginDto {

    private String countryName;

    private String selectedLanguage;

    private String applicationVersion;
}
