import Sidebar from '../components/Sidebar';

const DashboardLayout = ({ children }: { children: React.ReactNode }) => {
    return (
        <div className="flex min-h-screen">
            <Sidebar />
            <main className="flex-1 bg-gray-100 p-6">{children}</main>
        </div>
    );
};

export default DashboardLayout;
