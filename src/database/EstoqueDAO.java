/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import Classes.Estoque;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {
    
    private Connection conexao;
    private final PreparedStatement operacaoCreate;
    private final PreparedStatement operacaoAtualizar;
    private final PreparedStatement operacaoBuscaF;
    private final PreparedStatement operacaoBuscaP;
    private final PreparedStatement operacaoListar;
    private final PreparedStatement operacaoExcluirFilial;
    private final PreparedStatement operacaoCountFilial;
    private final PreparedStatement operacaoDistribuir;
    private final PreparedStatement operacaoDesativarFilial;
    private final PreparedStatement operacaoDistinctProduto;
    private final PreparedStatement operacaoFilialPorProduto;
    
    public EstoqueDAO() throws Exception{
        
        conexao = ConexaoJavaDB.getConnection();
        
        operacaoCreate = conexao.prepareStatement("INSERT INTO estoque(filial,produto,quantidade) VALUES(?,?,?)");
        operacaoAtualizar = conexao.prepareStatement("UPDATE estoque SET filial = ?, produto = ? WHERE filial = ? AND produto = ?");
        operacaoBuscaF = conexao.prepareStatement("SELECT * FROM estoque WHERE filial=?");
        operacaoBuscaP = conexao.prepareStatement("SELECT * FROM estoque WHERE produto=?");
        operacaoListar = conexao.prepareStatement("SELECT * FROM estoque");
        operacaoExcluirFilial = conexao.prepareStatement("DELETE FROM estoque WHERE filial = ?");
        operacaoCountFilial = conexao.prepareStatement("select count(produto) as qtdeFilial from LP3.ESTOQUE where produto = ?");
        operacaoDistribuir = conexao.prepareStatement("update estoque set quantidade = quantidade+? where produto = ?");
        operacaoDesativarFilial = conexao.prepareStatement("update estoque set quantidade = 0 where filial = ?");
        operacaoDistinctProduto = conexao.prepareStatement("select distinct produto from LP3.ESTOQUE");
        operacaoFilialPorProduto = conexao.prepareStatement("select filial from LP3.ESTOQUE where produto = ?");
    }
    
    public void atualizar(String filialNova, String produtoNovo, String filialAntiga, String produtoAntigo) throws SQLException{
        operacaoAtualizar.clearParameters();
        operacaoAtualizar.setString(1, filialNova);
        operacaoAtualizar.setString(2, produtoNovo);
        operacaoAtualizar.setString(3, filialAntiga);
        operacaoAtualizar.setString(4, produtoAntigo);
        
        operacaoAtualizar.executeUpdate();
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
    
    public List<Estoque> selectDistinctProduto() throws SQLException{
        ResultSet estq = operacaoDistinctProduto.executeQuery();
            List<Estoque> estoq = new ArrayList<>();
            while (estq.next()) {
                Estoque estoque = new Estoque();
                estoque.setFilial(null);
                estoque.setProduto(estq.getString("produto"));
                estoque.setQtd(0);

                estoq.add(estoque);
            }
            return estoq;
    }
    
        public List<Estoque> selectFilialPorProduto(String produto) throws SQLException{
            operacaoFilialPorProduto.clearParameters();
            operacaoFilialPorProduto.setString(1, produto);
            
            ResultSet estq = operacaoFilialPorProduto.executeQuery();
            List<Estoque> estoq = new ArrayList<>();
            while (estq.next()) {
                Estoque estoque = new Estoque();
                estoque.setFilial(estq.getString("filial"));
                estoque.setProduto(null);
                estoque.setQtd(0);

                estoq.add(estoque);
            }
            return estoq;
    }
    
    public void distribuirEstoque(String filial, String produto, int estoque) throws SQLException{
        operacaoDistribuir.clearParameters();
        operacaoDistribuir.setInt(1, estoque);
        operacaoDistribuir.setString(2, produto);
        operacaoDistribuir.executeUpdate();
        operacaoDistribuir.close();
        conexao.commit();
    }
    
        public void desativarFilial(String filial) throws SQLException{
            operacaoDesativarFilial.clearParameters();
            operacaoDesativarFilial.setString(1, filial);
            operacaoDesativarFilial.executeUpdate();
            conexao.commit();
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
    
        public List<Estoque> listarP(String produto) throws Exception{
        operacaoBuscaP.clearParameters();
        operacaoBuscaP.setString(1, produto);
        ResultSet listaP = operacaoBuscaP.executeQuery();
        
        List<Estoque> estoq = new ArrayList<>();
        
        while (listaP.next()){
            
            Estoque estoque = new Estoque();
            estoque.setFilial(listaP.getString("filial"));
            estoque.setProduto(listaP.getString("produto"));
            estoque.setQtd(listaP.getInt("quantidade"));
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
