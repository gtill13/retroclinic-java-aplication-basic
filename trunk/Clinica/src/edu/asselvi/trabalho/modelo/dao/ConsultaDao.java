package edu.asselvi.trabalho.modelo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.asselvi.trabalho.modelo.entidade.Consulta;

public class ConsultaDao extends DaoBase {
	
	public void inserirPadrao(Consulta consulta) throws DaoException {

		PacienteDao pacienteDao = new PacienteDao();
		MedicoDao medicoDao = new MedicoDao();
		MedicamentoDao medicamentoDao = new MedicamentoDao();
		PagamentoDao pagamentoDao = new PagamentoDao();
		
		conecta();
		
		executeUpdate("insert into consulta (id_medico, id_paciente, id_medicamento, id_pagamento, valor, datacriacao) values ( '"
				+ medicoDao.inserirComRetorno(consulta.getMedico())           + "', '" + pacienteDao.inserirComRetorno(consulta.getPaciente()) + "', '"
				+ medicamentoDao.inserirComRetorno(consulta.getMedicamento()) + "', '" + pagamentoDao.inserirComRetorno(consulta.getPagamento()) + "', '"
				+ consulta.getValor()                                         + "', '" + consulta.getDataCriacao()                         + "' ) ");
		
		commit();

		disconecta();
	}

	public void atualizar(Consulta consulta) throws DaoException {

		PacienteDao pacienteDao = new PacienteDao();
		MedicoDao medicoDao = new MedicoDao();
		MedicamentoDao medicamentoDao = new MedicamentoDao();
		PagamentoDao pagamentoDao = new PagamentoDao();
		conecta();

		pacienteDao.atualizar(consulta.getPaciente());
		medicoDao .atualizar(consulta.getMedico());
		medicamentoDao .atualizar(consulta.getMedicamento());
		pagamentoDao .atualizar(consulta.getPagamento());
		
		executeUpdate("update consulta set valor = '" + consulta.getValor()
				+ "', datacriacao = '" + consulta.getDataCriacao() + "' ");
		
		commit();

		disconecta();

	}
	
	public void deletar(Consulta consulta) throws DaoException {

		//EnderecoDao enderecoDao = new EnderecoDao();
		//ContatoDao contatoDao = new ContatoDao();
		conecta();

		executeUpdate("delete from consulta where id = '" + consulta.getId() + "' "); 

		//enderecoDao.deletar(paciente.getEndereco());
		//contatoDao.deletar(paciente.getContato());

		commit();

		disconecta();
	}

	public List<Consulta> buscaTodos () {
		
		final PacienteDao pacienteDao = new PacienteDao();
		final MedicoDao medicoDao = new MedicoDao();
		final MedicamentoDao medicamentoDao = new MedicamentoDao();
		final PagamentoDao pagamentoDao = new PagamentoDao();
		final List<Consulta> listConsultas = new ArrayList<Consulta>();
		
		executeQuery("select * from consulta", new Mapeador<Consulta>() {
			public void mapear(ResultSet rset) throws DaoException {
				try {
					while (rset.next()) {
						Consulta consulta = new Consulta();
						
						consulta.setId(rset.getLong("id"));
						consulta.setValor(rset.getDouble("valor"));
						consulta.setDataCriacao(rset.getDate("datacriacao"));
						
						consulta.setPaciente   (pacienteDao   .buscaPacientePeloId   (rset.getLong("id_paciente"   )));
						consulta.setMedico     (medicoDao     .buscaMedicoPeloId     (rset.getLong("id_medico"     )));
						consulta.setMedicamento(medicamentoDao.buscaMedicamentoPeloId(rset.getLong("id_medicamento")));
						consulta.setPagamento  (pagamentoDao  .buscaPagamentoPeloId  (rset.getLong("id_pagamento"  )));
										
						listConsultas.add(consulta);
					}
				} catch (SQLException e) {
					throw new DaoException("Banco de dados - Erro ao criar lista em " + this.getClass().toString(), e);
				} 
			}
		});
		
		return listConsultas;
	}
	
	public Consulta buscaConsultaPeloId(long id) {

		final Consulta consulta = new Consulta();

		executeQuery("select * from consulta where id = '" + id + "' ",
				new Mapeador<Consulta>() {
					public void mapear(ResultSet rset) throws DaoException {
						try {
							if (rset.next()) {

								PacienteDao pacienteDao = new PacienteDao();
								MedicoDao medicoDao = new MedicoDao();
								MedicamentoDao medicamentoDao = new MedicamentoDao();
								PagamentoDao pagamentoDao = new PagamentoDao();
								
								consulta.setId(rset.getLong("id"));
								consulta.setValor(rset.getDouble("valor"));
								consulta.setDataCriacao(rset.getDate("datacriacao"));
								
								consulta.setPaciente   (pacienteDao   .buscaPacientePeloId   (rset.getLong("id_paciente"   )));
								consulta.setMedico     (medicoDao     .buscaMedicoPeloId     (rset.getLong("id_medico"     )));
								consulta.setMedicamento(medicamentoDao.buscaMedicamentoPeloId(rset.getLong("id_medicamento")));
								consulta.setPagamento  (pagamentoDao  .buscaPagamentoPeloId  (rset.getLong("id_pagamento"  )));
							}
						} catch (SQLException e) {
							throw new DaoException(
									"Banco de dados - Erro ao criar lista em "
											+ this.getClass().toString(), e);
						} 
					}
				});

		return consulta;
	}
	
}