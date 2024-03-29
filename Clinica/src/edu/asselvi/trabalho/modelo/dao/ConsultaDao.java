package edu.asselvi.trabalho.modelo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.asselvi.trabalho.modelo.entidade.Consulta;
import edu.asselvi.trabalho.modelo.entidade.Contato;
import edu.asselvi.trabalho.modelo.entidade.ESexo;
import edu.asselvi.trabalho.modelo.entidade.Endereco;
import edu.asselvi.trabalho.modelo.entidade.Medicamento;
import edu.asselvi.trabalho.modelo.entidade.Medico;
import edu.asselvi.trabalho.modelo.entidade.Pagamento;

public class ConsultaDao extends DaoBase {
	
	public void inserirPadrao(Consulta consulta) throws DaoException {

		PacienteDao pacienteDao = new PacienteDao();
		MedicoDao medicoDao = new MedicoDao();
		MedicamentoDao medicamentoDao = new MedicamentoDao();
		PagamentoDao pagamentoDao = new PagamentoDao();
		
		pacienteDao.atualizar(consulta.getPaciente());
		medicoDao.atualizar(consulta.getMedico());
		
		conecta();
		
		executeUpdate("insert into consulta (id_medico, id_paciente, id_medicamento, id_pagamento) values ( '"
				+ consulta.getMedico().getId()           + "', '" + consulta.getPaciente().getId() + "', '"
				+ medicamentoDao.inserirComRetorno(consulta.getMedicamento()) + "', '" + pagamentoDao.inserirComRetorno(consulta.getPagamento()) + "' ) ");
		
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

	public void CriaDemo() {
		Consulta consulta = new Consulta();
		MedicoDao medicoDao = new MedicoDao();
		PacienteDao pacienteDao = new PacienteDao();
		Medicamento medicamento = new Medicamento();
		Pagamento pagamento = new Pagamento();
		Random random = new Random();
	
		for(int i = 0; i < 10; ++i)
		{
			
			long idMedico = 0;
			
			while (idMedico == 0)
			{
				idMedico = Math.abs(random.nextInt()%11);
			}
						
			consulta.setMedico(medicoDao.buscaMedicoPeloId(idMedico));

			long idPaciente = 0;
			
			while (idPaciente == 0)
			{
				idPaciente = Math.abs(random.nextInt()%11);
			}
						
			consulta.setPaciente(pacienteDao.buscaPacientePeloId(idPaciente));
			
			medicamento.setNome(stringAleatoria(12));
			medicamento.setDescricao(stringAleatoria(60));
			
			pagamento.setValor(random.nextDouble());
			
			consulta.setMedicamento(medicamento);
			consulta.setPagamento(pagamento);
			
			inserirPadrao(consulta);
		}
		
	}
	
}
