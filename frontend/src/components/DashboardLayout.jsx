import React from 'react';
import { Box, Flex, Heading, Text, VStack, HStack, Icon, useColorModeValue, Divider, Avatar, IconButton } from '@chakra-ui/react';
import { NavLink, Outlet, useNavigate, useLocation } from 'react-router-dom';
import { User, Package, ClipboardList, Users, CreditCard, LogOut, ShoppingCart } from 'lucide-react';
import logo from '../assets/logo.png';

const SidebarItem = ({ icon, label, to, onClick }) => {
  const location = useLocation();
  const isActive = location.pathname === to;

  return (
    <NavLink to={to} onClick={onClick} style={{ width: '100%' }}>
      <HStack
        spacing={4}
        w="full"
        p={3}
        borderRadius="xl"
        cursor="pointer"
        bg={isActive ? 'brand.500' : 'transparent'}
        color={isActive ? 'brown.900' : 'brown.200'}
        _hover={{
          bg: isActive ? 'brand.500' : 'brown.800',
          color: isActive ? 'brown.900' : 'brand.500',
        }}
        transition="all 0.2s"
      >
        <Icon as={icon} boxSize={5} />
        <Text fontWeight="medium">{label}</Text>
      </HStack>
    </NavLink>
  );
};

const DashboardLayout = () => {
  const navigate = useNavigate();
  const bgColor = useColorModeValue('brown.50', 'gray.900');
  const sidebarBg = 'brown.900';

  const [user, setUser] = React.useState(null);

  React.useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/'); // Change to root or login
  };

  return (
    <Flex h="100vh" w="100vw" bg={bgColor} overflow="hidden">
      {/* Sidebar */}
      <Flex
        w="250px"
        h="full"
        bg={sidebarBg}
        direction="column"
        p={5}
        justify="space-between"
        display={{ base: 'none', md: 'flex' }}
      >
        <VStack align="start" spacing={8} w="full">
          {/* Brand */}
          <HStack spacing={3}>
            <Box p={2} bg="brand.500" borderRadius="full">
              <img src={logo} alt="TeddyBite" style={{ width: '24px', height: '24px' }} />
            </Box>
            <Heading size="md" color="brand.500">TeddyBite</Heading>
          </HStack>

          {/* Navigation */}
          <VStack w="full" spacing={2}>
            <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" w="full" pl={3}>
              Management
            </Text>
            <SidebarItem icon={User} label="Profile" to="/dashboard/profile" />
            <SidebarItem icon={Package} label="Items" to="/dashboard/items" />
            <SidebarItem icon={ClipboardList} label="Orders" to="/dashboard/orders" />
            <SidebarItem icon={Users} label="Employees" to="/dashboard/employees" />
            <SidebarItem icon={CreditCard} label="Payments" to="/dashboard/payments" />

            <Divider borderColor="brown.700" my={4} />

            <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" w="full" pl={3}>
              Operations
            </Text>
            <SidebarItem icon={ShoppingCart} label="POS System" to="/order" />
          </VStack>
        </VStack>

        {/* User Profile / Logout */}
        {/* User Profile / Logout */}
        <Box w="full" bg="brown.800" borderRadius="xl" p={3}>
          <HStack justify="space-between" spacing={3}>
            <HStack spacing={3} overflow="hidden">
              <Avatar size="sm" name={user?.name || "Employee"} bg="brand.500" color="brown.900" />
              <VStack align="start" spacing={0} overflow="hidden">
                <Text fontSize="sm" fontWeight="bold" color="white" isTruncated w="full">{user?.name || "Employee"}</Text>
                <Text fontSize="xs" color="gray.400" textTransform="capitalize">{user?.position?.replace('_', ' ')?.toLowerCase() || "Staff"}</Text>
              </VStack>
            </HStack>
            <IconButton
              icon={<LogOut size={18} />}
              onClick={handleLogout}
              variant="ghost"
              color="gray.400"
              _hover={{ color: "red.300", bg: "whiteAlpha.200" }}
              aria-label="Logout"
              size="sm"
            />
          </HStack>
        </Box>
      </Flex>

      {/* Main Content */}
      <Box flex={1} h="full" overflowY="auto" p={8}>
        <Outlet />
      </Box>
    </Flex>
  );
};

export default DashboardLayout;
