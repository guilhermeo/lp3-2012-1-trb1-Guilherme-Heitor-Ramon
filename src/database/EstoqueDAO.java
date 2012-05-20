/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Classes.Estoque;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstoqueDAO {
    
    private Connection conexao;
    private final PreparedStatement operacaoCreate;
//    private final PreparedStatement operacaoAtualizar;
    private final PreparedStatement operacaoBuscaF;
//    private final PreparedStatement operacaoBuscaP;
    private final PreparedStatement operacaoListar;
    private final PreparedStatement operacaoExcluirFilial;
    private final PreparedStatement operacaoCountFilial;
    private final PreparedStatement operacaoDistribuir;
    private final PreparedStatement operacaoDesativarFilial;
    
    public EstoqueDAO() throws Exception{
        
        conexao = ConexaoJavaDB.getConnection();
        
        operacaoCreate = conexao.prepareStatement("INSERT INTO estoque(filial,produto,quantidade) VALUES(?,?,?)");
//        operacaoAtualizar = conexao.prepareStatement("UPDATE estoque SET filial = CURRENT_TIMESTAMP WHERE produto = ?");
        operacaoBuscaF = conexao.prepareStatement("SELECT * FROM estoque WHERE filial=?");
//        operacaoBuscaP = conexao.prepareStatement("SELECT * FROM estoque WHERE produto=?");
        operacaoListar = conexao.prepareStatement("SELECT * FROM estoque");
        operacaoExcluirFilial = conexao.prepareStatement("DELETE FROM estoque WHERE filial = ?");
        operacaoCountFilial = conexao.prepareStatement("select count(produto) as qtdeFilial from LP3.ESTOQUE where produto = ?");
        operacaoDistribuir = conexao.prepareStatement("update estoque set quantidade = quantidade+? where produto = ?");
        operacaoDesativarFilial = conexao.prepareStatement("update estoque set quantidade = 0 where filial = ?");
    }
    
    public void criar(Estoque estoque) throws Exception {
            //pega os dados do formulario.
            operacaoCreate.clearParameters();
            operacaoCreate.setString(1, estoque.getFilial());
            operacaoCreate.setString(2, estoque.getProduto());
            operacaoCreate.setInt(3, estoque.getQtd());
            //executa o script
            operacaoCreate.executeUpdate();
    }
    
    public void excluirFilial(String filial) throws SQLException{
        operacaoExcluirFilial.clearParameters();
        operacaoExcluirFilial.setString(1, filial);
        operacaoExcluirFilial.executeUpdate();
    }
    
    public int countFilial (String produto) throws SQLException{
        int qtde = 0;
        operacaoCountFilial.clearParameters();
        operacaoCountFilial.setString(1, produto);
        ResultSet rs = operacaoCountFilial.executeQuery();
        while(rs.next()){
            qtde = rs.getInt("qtdeFilial");
        }
        return qtde;
    }
    
    public void distribuirEstoque(String filial, String produto, int estoque) throws SQLException{
        operacaoDistribuir.clearParameters();
        operacaoDistribuir.setInt(1, estoque);
        operacaoDistribuir.setString(2, produto);
        operacaoDistribuir.executeUpdate();
    }
    
        public void desativarFilial(String filial, String produto, int estoque) throws SQLException{
        operacaoDistribuir.clearParameters();
        operacaoDistribuir.setInt(1, estoque);
        operacaoDistribuir.setString(2, produto);
        operacaoDistribuir.executeUpdate();
    }
    
//    public void excluirDados(Estoque estoque) throws Exception {
//            operacaoExcluir.clearParameters();
//            operacaoExcluir.setString(1, estoque.getFilial());
//            operacaoExcluir.executeUpdate();        
//    }
//    
    public List<Estoque> listAll() throws Exception {
            ResultSet estq = operacaoListar.executeQuery();
            List<Estoque> estoq = new ArrayList<>();
            while (estq.next()) {
                Estoque estoque = new Estoque();
                estoque.setFilial(estq.getString("filial"));
                estoque.setProduto(estq.getString("produto"));
                estoque.setQtd(estq.getInt("quantidade"));

                estoq.add(estoque);
            }
            return estoq;
    }
    
    public List<Estoque> listarF(String filial) throws Exception{
        operacaoBuscaF.clearParameters();
        operacaoBuscaF.setString(1, filial);
        ResultSet listaF = operacaoBuscaF.executeQuery();
        
        List<Estoque> estoq = new ArrayList<>();
        
        while (listaF.next()){
            
            Estoque estoque = new Estoque();
            estoque.setFilial(listaF.getString("filial"));
            estoque.setProduto(listaF.getString("produto"));
            estoque.setQtd(listaF.getInt("quantidade"));
            estoq.add(estoque);
        }
        
        return estoq;
    }
    
//    public ResultSet listarP(String produto) throws Exception{
//    
//            Statement statement = conexao.createStatement();
//            String qry = "SELECT * FROM estoque WHERE produto=" + produto;
//            ResultSet rs = statement.executeQuery(qry);
//    
//            return rs;
//    }
    
}
