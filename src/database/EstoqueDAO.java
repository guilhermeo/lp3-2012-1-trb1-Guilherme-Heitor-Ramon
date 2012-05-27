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
    private final PreparedStatement operacaoListar;
    private final PreparedStatement operacaoExcluirFilial;
    private final PreparedStatement operacaoCountFilial;
    private final PreparedStatement operacaoDistribuir;
    private final PreparedStatement operacaoDesativarFilial;
    private final PreparedStatement operacaoFilialPorProduto;
    private final PreparedStatement operacaoUpdateDistribuir;
    private final PreparedStatement operacaoTransporte;
    
    public EstoqueDAO() throws Exception{
        
        conexao = ConexaoJavaDB.getConnection();
        
        operacaoCreate = conexao.prepareStatement("INSERT INTO estoque(filial,produto,quantidade) VALUES(?,?,?)");
        operacaoAtualizar = conexao.prepareStatement("UPDATE estoque SET quantidade = ? WHERE filial = ? AND produto = ?");
        operacaoListar = conexao.prepareStatement("SELECT * FROM estoque order by produto");
        operacaoExcluirFilial = conexao.prepareStatement("DELETE FROM estoque WHERE filial = ?");
        operacaoCountFilial = conexao.prepareStatement("select count(filial) as qtdeFilial from LP3.ESTOQUE where produto = ?");
        operacaoDistribuir = conexao.prepareStatement("update estoque set quantidade = quantidade+? where produto = ? AND filial <> ?");
        operacaoUpdateDistribuir = conexao.prepareStatement("update estoque set quantidade = quantidade - ? where filial = ? and produto = ?");
        operacaoDesativarFilial = conexao.prepareStatement("update estoque set quantidade = 0 where filial = ?");
        operacaoFilialPorProduto = conexao.prepareStatement("select filial from LP3.ESTOQUE where produto = ? and filial <> ?");
        operacaoTransporte = conexao.prepareStatement("update estoque set quantidade = quantidade+? where produto = ? AND filial = ?");
    }
    
    public void receberProduto(int qtde, String filial, String produto) throws SQLException{
        operacaoTransporte.clearParameters();
        operacaoTransporte.setInt(1, qtde);
        operacaoTransporte.setString(2, produto);
        operacaoTransporte.setString(3, filial);
        
        operacaoTransporte.executeUpdate();
        conexao.commit();
    }
    
    public void atualizar(String filial, String produto, int qtde) throws SQLException{
        operacaoAtualizar.clearParameters();
        operacaoAtualizar.setInt(1, qtde);
        operacaoAtualizar.setString(2, filial);
        operacaoAtualizar.setString(3, produto);
        
        operacaoAtualizar.executeUpdate();
        conexao.commit();
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
    
    public int countFilial (String filial) throws SQLException{
        int qtde = 0;
        operacaoCountFilial.clearParameters();
        operacaoCountFilial.setString(1, filial);
        ResultSet rs = operacaoCountFilial.executeQuery();
        while(rs.next()){
            qtde = rs.getInt("qtdeFilial");
        }
        return qtde;
    }
    
        public List<Estoque> selectFilialPorProduto(String produto, String filial) throws SQLException{
            operacaoFilialPorProduto.clearParameters();
            operacaoFilialPorProduto.setString(1, produto);
            operacaoFilialPorProduto.setString(2, filial);
                        
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
    
    public void distribuirEstoque(String produto, int estoque, String filialOrigem) throws SQLException{
        
//        ("update estoque set quantidade = quantidade+? where produto = ? AND filial <> ?");
        
        operacaoDistribuir.clearParameters();
        operacaoDistribuir.setInt(1, estoque);
        operacaoDistribuir.setString(2, produto);
        operacaoDistribuir.setString(3, filialOrigem);
        operacaoDistribuir.executeUpdate();
        operacaoDistribuir.close();
        conexao.commit();
    }
    
        public void updateEstoque(int estoque, String filial, String produto) throws SQLException{
        
        //update estoque set quantidade = quantidade - ? where filial = ? and produto = ?
        operacaoUpdateDistribuir.clearParameters();
        operacaoUpdateDistribuir.setInt(1, estoque);
        operacaoUpdateDistribuir.setString(2, filial);
        operacaoUpdateDistribuir.setString(3, produto);
        operacaoUpdateDistribuir.executeUpdate();
        operacaoUpdateDistribuir.close();
        conexao.commit();
    }
    
    public void desativarFilial(String filial) throws SQLException{
        operacaoDesativarFilial.clearParameters();
        operacaoDesativarFilial.setString(1, filial);
        operacaoDesativarFilial.executeUpdate();
        conexao.commit();
    }
     
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
    
    
}
