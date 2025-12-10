import React from 'react';
import {
  Box, Flex, Heading, Text, VStack, HStack, Icon, useColorModeValue, Divider,
  Avatar, IconButton, Drawer, DrawerContent, useDisclosure, DrawerOverlay, DrawerCloseButton
} from '@chakra-ui/react';
import { NavLink, Outlet, useNavigate, useLocation } from 'react-router-dom';
import { User, Package, ClipboardList, Users, CreditCard, LogOut, ShoppingCart, Menu, X } from 'lucide-react';
import logo from '../assets/logo.png';

const SidebarItem = ({ icon, label, to, onClick, ...rest }) => {
  const location = useLocation();
  const isActive = location.pathname === to;

  return (
    <NavLink to={to} onClick={onClick} style={{ width: '100%' }} {...rest}>
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

const SidebarContent = ({ onClose, ...rest }) => (
  <Box
    bg="brown.900"
    w={{ base: 'full', md: 60 }}
    pos="fixed"
    h="full"
    {...rest}
  >
    <Flex h="20" alignItems="center" mx="8" justifyContent="space-between">
      <HStack spacing={3}>
        <Box p={2} bg="brand.500" borderRadius="full">
          <img src={logo} alt="TeddyBite" style={{ width: '24px', height: '24px' }} />
        </Box>
        <Heading size="md" color="brand.500">TeddyBite</Heading>
      </HStack>
      <IconButton
        display={{ base: 'flex', md: 'none' }}
        onClick={onClose}
        variant="ghost"
        color="white"
        _hover={{ bg: "whiteAlpha.200", color: "red.300" }}
        aria-label="Close menu"
        icon={<X size={24} />}
      />
    </Flex>
    <VStack align="start" spacing={1} w="full" px={4}>
      <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" w="full" pl={3} mt={4} mb={2}>
        Management
      </Text>
      <SidebarItem icon={User} label="Profile" to="/dashboard/profile" onClick={onClose} />
      <SidebarItem icon={Package} label="Items" to="/dashboard/items" onClick={onClose} />
      <SidebarItem icon={ClipboardList} label="Orders" to="/dashboard/orders" onClick={onClose} />
      <SidebarItem icon={Users} label="Employees" to="/dashboard/employees" onClick={onClose} />
      <SidebarItem icon={CreditCard} label="Payments" to="/dashboard/payments" onClick={onClose} />

      <Divider borderColor="brown.700" my={4} />

      <Text color="gray.500" fontSize="xs" fontWeight="bold" textTransform="uppercase" w="full" pl={3} mb={2}>
        Operations
      </Text>
      <SidebarItem icon={ShoppingCart} label="POS System" to="/order" target="_blank" onClick={onClose} />
    </VStack>
  </Box>
);

const UserProfileSection = ({ user, handleLogout }) => (
  <Box w="full" bg="brown.800" borderRadius="xl" p={3} mb={4}>
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
);


const MobileNav = ({ onOpen, user, handleLogout, ...rest }) => {
  return (
    <Flex
      ml={{ base: 0, md: 60 }}
      px={{ base: 4, md: 24 }}
      height="20"
      alignItems="center"
      bg="brown.900"
      borderBottomWidth="1px"
      borderBottomColor="brown.700"
      justifyContent="space-between"
      display={{ base: 'flex', md: 'none' }}
      {...rest}
    >
      <IconButton
        variant="ghost"
        onClick={onOpen}
        aria-label="open menu"
        icon={<Menu />}
        color="brand.500"
        _hover={{ bg: "brown.800", color: "white" }}
      />

      <HStack spacing={3}>
        <Box p={2} bg="brand.500" borderRadius="full">
          <img src={logo} alt="TeddyBite" style={{ width: '20px', height: '20px' }} />
        </Box>
        <Heading size="sm" color="brand.500">TeddyBite</Heading>
      </HStack>
    </Flex>
  );
};

const DashboardLayout = () => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const navigate = useNavigate();
  const bgColor = useColorModeValue('brown.50', 'gray.900');

  // Sidebar logic
  const [user, setUser] = React.useState(null);

  React.useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    navigate('/');
  };

  return (
    <Box minH="100vh" bg={bgColor}>
      <SidebarContent
        onClose={() => onClose}
        display={{ base: 'none', md: 'block' }}
      >
        {/* We need to inject the UserProfile section into the desktop sidebar manually or compositionally */}
      </SidebarContent>

      {/* Helper to show user profile at bottom of specific desktop sidebar instance */}
      <Box display={{ base: 'none', md: 'block' }} pos="fixed" bottom={0} w={60} p={4} zIndex={20}>
        <UserProfileSection user={user} handleLogout={handleLogout} />
      </Box>

      <Drawer
        autoFocus={false}
        isOpen={isOpen}
        placement="left"
        onClose={onClose}
        returnFocusOnClose={false}
        onOverlayClick={onClose}
        size="full"
      >
        <DrawerContent>
          <SidebarContent onClose={onClose} />
          <Box pos="absolute" bottom={0} w="full" p={4}>
            <UserProfileSection user={user} handleLogout={handleLogout} />
          </Box>
        </DrawerContent>
      </Drawer>

      {/* Mobile Nav */}
      <MobileNav onOpen={onOpen} user={user} handleLogout={handleLogout} />

      <Box ml={{ base: 0, md: 60 }} p="4">
        <Outlet />
      </Box>
    </Box>
  );
};

export default DashboardLayout;
