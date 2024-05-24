package com.mil0812.persistence.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
  T mapRow(ResultSet rs) throws SQLException;

}
