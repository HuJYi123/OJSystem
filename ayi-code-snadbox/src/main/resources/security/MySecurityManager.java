
import java.io.FileDescriptor;
import java.security.Permission;

/**
 * className:DefaultSecurityManager
 * Package:com.example.springboot_02.security
 * Description: 默认安全管理器
 *
 * @Date: 2023/12/17 18:08
 * @Author:hjy
 */
public class MySecurityManager extends SecurityManager{

    /**
     * 检查所有的权限
     * @param perm   the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }

    /**
     * 检查程序是否允许文件执行
     * @param cmd   the specified system command.
     */
    @Override
    public void checkExec(String cmd) {
        super.checkExec(cmd);
    }

    /**
     * 检查程序是否允许文件删除
     * @param file   the system-dependent filename.
     */
    @Override
    public void checkDelete(String file) {
        super.checkDelete(file);
    }

    /**
     * 检查程序是否允许文件可读
     * @param fd   the system-dependent file descriptor.
     */
    @Override
    public void checkRead(FileDescriptor fd) {
        super.checkRead(fd);
    }


    /**
     * 检查程序是否允许文件可写
     * @param fd   the system-dependent file descriptor.
     */
    @Override
    public void checkWrite(FileDescriptor fd) {
        super.checkWrite(fd);
    }

    /**
     * 检查程序是否允许网络连接
     * @param host   the host name.
     * @param port   the port number.
     */
    @Override
    public void checkConnect(String host, int port) {
        super.checkConnect(host, port);
    }
}
