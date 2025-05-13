import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import DashboardLayout from './layout/DashboardLayout';
import Productos from './pages/Productos/Index';
import Pedidos from './pages/Pedidos/Index';
import Compras from './pages/Compras/Index';
import Ventas from './pages/Ventas/Index';
import AgregarProducto from "./subpagesproducto/AgregarProducto/agregarProducto.tsx";
import BuscarProducto from "./subpagesproducto/BuscarProducto/Index.tsx";
import GestionarPedidos from './subpagesPedido/GestionarPedidos/gestionarPedidos.tsx';
import BuscarPedidos from './subpagesPedido/ListarPedidos/listarPedidos.tsx';
import VerInventario from './subpagesInventario/GestionarInventario/gestionarInventario.tsx';
import GestionarResenas from "./subpagesResenia/MostrarResena/mostrarResena.tsx";
import GestionarClientes from "./subpagesClientes/RegistrarClientes/registrarClientes.tsx";
import BuscarClientes from "./subpagesClientes/BuscarClientes/buscarClientes.tsx"

function App() {
  return (
      <Router>
        <DashboardLayout>
          <Routes>
            <Route path="/productos" element={<Productos />} />
              <Route path="/productos/agregar" element={<AgregarProducto />} />
              <Route path="/productos/Index" element={<BuscarProducto  />} />
              <Route path="/pedidos" element={<Pedidos />} />
              <Route path="/pedidos/gestionar" element={<GestionarPedidos />} />
              <Route path="/pedidos/listar" element={<BuscarPedidos />} />
              <Route path="/inventario/ver" element={<VerInventario />} />
            <Route path="/resenias/mostrar" element={<GestionarResenas />} />
            <Route path="/clientes/registrar" element={<GestionarClientes />} />
            <Route path="/clientes/buscar" element={<BuscarClientes />} />
            <Route path="/compras" element={<Compras />} />
            <Route path="/ventas" element={<Ventas />} />
            <Route path="/resenias/mostrar" element={<GestionarResenas />}/>
          </Routes>
        </DashboardLayout>
      </Router>
  );
}

export default App;
