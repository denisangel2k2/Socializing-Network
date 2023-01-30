package com.socialnetwork.app.repository;

import com.socialnetwork.app.domain.Message;
import com.socialnetwork.app.domain.User;
import com.socialnetwork.app.exceptions.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo extends AbstractRepo<Message> {

    private UserRepo repoUser;
    public MessageRepo(String url, String userName, String password, UserRepo _repoUser) {
        super(url, userName, password);
        this.repoUser=_repoUser;
        loadData();
    }

    @Override
    protected Message extractEntity(String line) {
        return null;
    }

    @Override
    protected List<Message> extractEntity(ResultSet set) throws SQLException {
        List<Message> messageList=new ArrayList<>();
        while (set.next()){
            Integer id=set.getInt("id");
            Integer id_from=set.getInt("id_from");
            Integer id_to=set.getInt("id_to");
            String date=set.getString("date");
            String messageText=set.getString("message");

            try{
                User user_from=repoUser.findElement(id_from);
                User user_to=repoUser.findElement(id_to);
                Message message= new Message(user_from,user_to,messageText);
                message.setId(id);
                message.setTimeSent(date);
                messageList.add(message);
            }
            catch (RepoException ex){
                ex.printStackTrace();
            }

        }
        return messageList;
    }

    @Override
    protected PreparedStatement ps_getAll(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM messages");
    }

    @Override
    protected void storeEntity(Message entity, Connection connection) throws SQLException, RepoException {
        String sql="INSERT INTO messages(id,id_from,id_to,date,message) values (?,?,?,?,?)";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,entity.getId());
        ps.setInt(2,entity.getSender().getId());
        ps.setInt(3,entity.getReceiver().getId());
        ps.setString(4, entity.getTimeSentString());
        ps.setString(5,entity.getMessage());
        ps.executeUpdate();
    }

    @Override
    protected void deleteEntity(Message entity, Connection connection) throws SQLException {
        String sql = "DELETE FROM messages WHERE id=?";
        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,entity.getId());
        ps.executeUpdate();
    }



}
