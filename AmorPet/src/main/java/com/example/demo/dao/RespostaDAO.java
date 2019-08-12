package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Resposta;
import com.example.demo.model.Usuario;

@Repository
public interface RespostaDAO extends JpaRepository<Resposta, Integer> {

	@Query("select r from Resposta r where r.idProcesso = (select p from Processo p where p.idUsuario = :idUsuario)")
	List<Resposta> findByRespostas(Usuario idUsuario);
}