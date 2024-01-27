package com.ayi.ayiojbackendcommom.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Context implements Serializable {

	private String token;

	private String userName;
}
