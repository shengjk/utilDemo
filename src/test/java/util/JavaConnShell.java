package util;

import org.apache.log4j.Logger;
import org.apache.sshd.ClientChannel;
import org.apache.sshd.ClientSession;
import org.apache.sshd.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.util.NoCloseOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by weizheng on 2016/7/14.
 */
public class JavaConnShell {
    private static Logger logger = Logger.getLogger(JavaConnShell.class);
    
    
    /**
     *
     * @param username
     * @param password
     * @param hostName
     * @param port
     * @param command
     */
    public static void ConnShell(String username,String password,String hostName,int port,String[] command ){
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try {
            ClientSession session = client.connect(hostName, port).await().getSession();
            int authState = ClientSession.WAIT_AUTH;
            while ((authState & ClientSession.WAIT_AUTH) != 0){
                AuthFuture authFuture = session.authPassword(username, password);
                authFuture.addListener(new SshFutureListener<AuthFuture>()
                {
                    @Override
                    public void operationComplete(AuthFuture arg0)
                    {
                        System.out.println("Authentication completed with " + ( arg0.isSuccess() ? "success" : "failure"));
                    }
                });
                authState = session.waitFor(ClientSession.WAIT_AUTH | ClientSession.CLOSED | ClientSession.AUTHED, 0);
            }
            if ((authState & ClientSession.CLOSED) != 0) {
                System.err.println("error");
                System.exit(-1);
            }
            ClientChannel channel  = session.createChannel(ClientChannel.CHANNEL_SHELL);;
            if (command!=null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Writer w = new OutputStreamWriter(baos);
                for (String cmd : command) {
                    w.append(cmd).append("\n");
                }
                w.append("exit\n");
                w.close();
                channel.setIn(new ByteArrayInputStream(baos.toByteArray()));
            }
            channel.setOut(new NoCloseOutputStream(System.out));
            channel.setErr(new NoCloseOutputStream(System.err));
            channel.open().await();
            channel.waitFor(ClientChannel.CLOSED, 0);
            session.close(true);
        } catch (Exception e){
            logger.error("执行hbase迁移异常，异常信息：{}",e);
            throw new RuntimeException(e);
        } finally{
            client.stop();
        }
    }
    
    public static void main(String[] args) {
        ConnShell("cdh","cdh123456!","cdh02",22,new String[]{"ps -ef|grep kyin"});
    }
}
