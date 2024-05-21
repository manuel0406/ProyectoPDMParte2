package sv.edu.ues.fia.telollevoya.Reservaciones;

public class DetallePedidoR {
    private int idDetallePedido;
    private int idProducto;
    private int idReservacion;
    private int cantidadDetalle;
    private double subTotal;


    public DetallePedidoR() {
    }

    public DetallePedidoR(int idDetallePedido, int idProducto, int idReservacion, int cantidadDetalle, double subTotal) {
        this.idDetallePedido = idDetallePedido;
        this.idProducto = idProducto;
        this.idReservacion = idReservacion;
        this.cantidadDetalle = cantidadDetalle;
        this.subTotal = subTotal;
    }

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(int idReservacion) {
        this.idReservacion = idReservacion;
    }

    public int getCantidadDetalle() {
        return cantidadDetalle;
    }

    public void setCantidadDetalle(int cantidadDetalle) {
        this.cantidadDetalle = cantidadDetalle;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
