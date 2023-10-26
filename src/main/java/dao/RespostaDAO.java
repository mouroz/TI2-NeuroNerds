package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Calendar;
import model.Resposta;

import model.Pergunta;

public class RespostaDAO extends DAO{

    public RespostaDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

	
}
