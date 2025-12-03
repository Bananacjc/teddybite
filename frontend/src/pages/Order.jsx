import React, { useState } from 'react';
import {
  Box,
  Flex,
  Grid,
  Heading,
  Text,
  Image,
  Input,
  InputGroup,
  InputLeftElement,
  Button,
  VStack,
  HStack,
  IconButton,
  Badge,
  useTheme,
  Avatar,
  Tooltip,
  Divider,
  Container,
  Drawer,
  DrawerBody,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  DrawerContent,
  DrawerCloseButton,
  useDisclosure,
  useToast,
} from '@chakra-ui/react';
import { SearchIcon, AddIcon, MinusIcon, DeleteIcon } from '@chakra-ui/icons';
import { 
  Hamburger, 
  Pizza, 
  CupSoda, 
  IceCream, 
  Popcorn, 
  Receipt, 
  CreditCard, 
  ShoppingCart,
  Star
} from 'lucide-react';
import axios from 'axios';
import { motion, AnimatePresence } from 'framer-motion';
import logo from '../assets/logo.png';

// Motion Components
const MotionBox = motion(Box);
const MotionFlex = motion(Flex);
const MotionGrid = motion(Grid);

// Mock Data
const CATEGORIES = [
  { id: 'burger', name: 'Burgers', icon: <Hamburger size={24} /> },
  { id: 'pizza', name: 'Pizza', icon: <Pizza size={24} /> },
  { id: 'drink', name: 'Drinks', icon: <CupSoda size={24} /> },
  { id: 'dessert', name: 'Dessert', icon: <IceCream size={24} /> },
  { id: 'snack', name: 'Snacks', icon: <Popcorn size={24} /> },
];

const MENU_ITEMS = [
  { id: 1, category: 'burger', name: 'Teddy Classic', price: 8.99, image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=500&q=60', desc: 'Beef patty, cheddar, lettuce, tomato, house sauce', rating: 4.8 },
  { id: 2, category: 'burger', name: 'Double Trouble', price: 12.99, image: 'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?auto=format&fit=crop&w=500&q=60', desc: 'Double beef, double cheese, bacon, onion rings', rating: 4.9 },
  { id: 3, category: 'burger', name: 'Chicken Crunch', price: 9.50, image: 'https://images.unsplash.com/photo-1615557960916-5f4791effe9d?auto=format&fit=crop&w=500&q=60', desc: 'Crispy chicken, spicy mayo, pickles', rating: 4.5 },
  { id: 4, category: 'pizza', name: 'Margherita', price: 10.00, image: 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=500&q=60', desc: 'Tomato sauce, mozzarella, basil', rating: 4.7 },
  { id: 5, category: 'pizza', name: 'Pepperoni Feast', price: 14.50, image: 'https://images.unsplash.com/photo-1628840042765-356cda07504e?auto=format&fit=crop&w=500&q=60', desc: 'Double pepperoni, extra cheese', rating: 4.8 },
  { id: 6, category: 'drink', name: 'Honey Lemon Tea', price: 3.50, image: 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=500&q=60', desc: 'Freshly brewed tea with honey and lemon', rating: 4.6 },
  { id: 7, category: 'drink', name: 'Berry Smoothie', price: 5.00, image: 'https://images.unsplash.com/photo-1623593688280-a503c00213cb?auto=format&fit=crop&w=500&q=60', desc: 'Mixed berries, yogurt, mint', rating: 4.9 },
  { id: 8, category: 'snack', name: 'Golden Fries', price: 3.99, image: 'https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?auto=format&fit=crop&w=500&q=60', desc: 'Crispy salted french fries', rating: 4.4 },
  { id: 9, category: 'snack', name: 'Onion Rings', price: 4.50, image: 'https://images.unsplash.com/photo-1639024471283-03518883512d?auto=format&fit=crop&w=500&q=60', desc: 'Battered and fried onion rings', rating: 4.3 },
  { id: 10, category: 'dessert', name: 'Choco Lava', price: 6.50, image: 'https://images.unsplash.com/photo-1624353365286-3f8d62daad51?auto=format&fit=crop&w=500&q=60', desc: 'Warm chocolate cake with molten center', rating: 5.0 },
];

const CartSection = ({ cart, updateQty, removeFromCart, subtotal, tax, total, onCheckout }) => (
  <Flex
    direction="column"
    h="full"
    bg="white"
    borderRadius={{ base: 0, lg: "30px" }}
    boxShadow={{ base: "none", lg: "0 20px 50px rgba(62, 39, 35, 0.1)" }}
    overflow="hidden"
  >
    <Box p={8} pb={4}>
      <Heading size="lg" color="brown.900" mb={2}>Order Menu</Heading>
      <Text color="gray.400" fontSize="sm">
        Order #092834 • {new Date().toLocaleDateString()}
      </Text>
    </Box>

    <Box flex={1} overflowY="auto" px={6} py={2} css={{
      '&::-webkit-scrollbar': { width: '4px' },
      '&::-webkit-scrollbar-track': { background: 'transparent' },
      '&::-webkit-scrollbar-thumb': { background: '#E0E0E0', borderRadius: '2px' },
    }}>
      {cart.length === 0 ? (
        <Flex direction="column" align="center" justify="center" h="full" color="gray.300">
          <Box mb={4} opacity={0.5}><Receipt size={64} /></Box>
          <Text fontSize="lg" fontWeight="medium">No items yet</Text>
          <Text fontSize="sm">Select items from the menu</Text>
        </Flex>
      ) : (
        <VStack spacing={4} align="stretch">
          <AnimatePresence>
            {cart.map(item => (
              <MotionBox
                key={item.id}
                initial={{ opacity: 0, x: 20 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: -20 }}
                layout
              >
                <Flex
                  align="center"
                  bg="brand.50"
                  p={3}
                  borderRadius="2xl"
                  role="group"
                >
                  <Image
                    src={item.image}
                    boxSize="65px"
                    borderRadius="xl"
                    objectFit="cover"
                    mr={4}
                    boxShadow="sm"
                  />
                  <Box flex={1}>
                    <Text fontWeight="bold" color="brown.900" fontSize="md">{item.name}</Text>
                    <Text fontWeight="bold" color="brand.600" fontSize="sm">
                      RM {(item.price * item.qty).toFixed(2)}
                    </Text>
                  </Box>
                  
                  <HStack spacing={3} bg="white" borderRadius="xl" px={2} py={1} boxShadow="sm">
                    <IconButton
                      icon={<MinusIcon />}
                      size="xs"
                      variant="ghost"
                      color="brown.900"
                      onClick={() => item.qty > 1 ? updateQty(item.id, -1) : removeFromCart(item.id)}
                      aria-label="Decrease"
                      _hover={{ bg: 'gray.100' }}
                    />
                    <Text fontWeight="bold" fontSize="sm" w="16px" textAlign="center">{item.qty}</Text>
                    <IconButton
                      icon={<AddIcon />}
                      size="xs"
                      variant="ghost"
                      color="brown.900"
                      onClick={() => updateQty(item.id, 1)}
                      aria-label="Increase"
                      _hover={{ bg: 'gray.100' }}
                    />
                  </HStack>
                </Flex>
              </MotionBox>
            ))}
          </AnimatePresence>
        </VStack>
      )}
    </Box>

    {/* Footer Summary */}
    <Box p={8} bg="white" borderTop="1px dashed" borderColor="gray.200">
      <VStack spacing={3} mb={6}>
        <Flex w="full" justify="space-between" color="gray.500" fontSize="sm">
          <Text>Subtotal</Text>
          <Text fontWeight="medium">RM {subtotal.toFixed(2)}</Text>
        </Flex>
        <Flex w="full" justify="space-between" color="gray.500" fontSize="sm">
          <Text>Tax (10%)</Text>
          <Text fontWeight="medium">RM {tax.toFixed(2)}</Text>
        </Flex>
        <Divider />
        <Flex w="full" justify="space-between" align="center" pt={2}>
          <Text color="brown.900" fontWeight="bold" fontSize="lg">Total</Text>
          <Text color="brown.900" fontWeight="800" fontSize="2xl">RM {total.toFixed(2)}</Text>
        </Flex>
      </VStack>
      
      <Button
        w="full"
        h="65px"
        bg="brown.900"
        color="brand.500"
        fontSize="lg"
        fontWeight="bold"
        borderRadius="2xl"
        _hover={{ 
          bg: 'brown.800', 
          transform: 'translateY(-2px)',
            boxShadow: '0 10px 20px rgba(62, 39, 35, 0.3)'
        }}
        _active={{ transform: 'translateY(0)' }}
        leftIcon={<CreditCard size={20} />}
        isDisabled={cart.length === 0}
        onClick={onCheckout}
      >
        Checkout
      </Button>
    </Box>
  </Flex>
);

const OrderPage = () => {
  const [activeCategory, setActiveCategory] = useState('burger');
  const [cart, setCart] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const { isOpen, onOpen, onClose } = useDisclosure();
  const theme = useTheme();
  const toast = useToast();

  const handleCheckout = async () => {
    const orderData = {
      orderItems: cart.map(item => ({
        itemId: item.id.toString(),
        itemName: item.name,
        unitPrice: item.price,
        quantity: item.qty,
        lineTotal: parseFloat((item.price * item.qty).toFixed(2))
      })),
      subTotal: subtotal,
      tax: tax,
      totalAmount: total,
      payment: {
        method: "CASH",
        transactionId: "TX" + Date.now().toString().slice(-6),
        amount: total,
        status: "Paid"
      },
      status: "Completed",
      createdAt: new Date().toISOString()
    };

    try {
      const response = await axios.post('http://localhost:8081/api/orders', orderData);

      toast({
        title: "Order Placed Successfully!",
        description: `Order ID: ${response.data.id}`,
        status: "success",
        duration: 5000,
        isClosable: true,
        position: "top",
      });
      setCart([]);
    } catch (error) {
      console.error("Order Error:", error);
      toast({
        title: "Connection Error",
        description: error.response?.data?.message || "Could not connect to the backend server. Make sure it's running!",
        status: "error",
        duration: 5000,
        isClosable: true,
        position: "top",
      });
    }
  };

  const filteredItems = MENU_ITEMS.filter(item =>  
    item.category === activeCategory && 
    item.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const addToCart = (item) => {
    setCart(prev => {
      const existing = prev.find(i => i.id === item.id);
      if (existing) {
        return prev.map(i => i.id === item.id ? { ...i, qty: i.qty + 1 } : i);
      }
      return [...prev, { ...item, qty: 1 }];
    });
  };

  const updateQty = (id, delta) => {
    setCart(prev => prev.map(item => {
      if (item.id === id) {
        const newQty = Math.max(0, item.qty + delta);
        return { ...item, qty: newQty };
      }
      return item;
    }).filter(item => item.qty > 0));
  };

  const removeFromCart = (id) => {
    setCart(prev => prev.filter(item => item.id !== id));
  };

  const subtotal = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);
  const tax = subtotal * 0.1;
  const total = subtotal + tax;

  return (
    <Flex h="100vh" w="100vw" overflow="hidden" bg="brown.50" position="relative" direction={{ base: 'column', md: 'row' }}>
      {/* Sidebar Navigation */}
      <Flex
        direction={{ base: 'row', md: 'column' }}
        w={{ base: '100%', md: '100px' }}
        h={{ base: '80px', md: '96vh' }}
        m={{ base: 0, md: '2vh' }}
        bg="brown.900"
        borderRadius={{ base: '20px 20px 0 0', md: '30px' }}
        boxShadow="0 8px 32px rgba(0, 0, 0, 0.2)"
        zIndex={20}
        align="center"
        justify={{ base: 'space-around', md: 'flex-start' }}
        py={{ base: 2, md: 8 }}
        position={{ base: 'fixed', md: 'relative' }}
        bottom={{ base: 0, md: 'auto' }}
      >
        <Box mb={{ base: 0, md: 10 }} p={2} bg="brand.500" borderRadius="full" boxShadow="md" display={{ base: 'none', md: 'block' }}>
          <Image src={logo} boxSize="45px" objectFit="contain" />
        </Box>
        
        <Flex direction={{ base: 'row', md: 'column' }} gap={6} w="full" justify="center" align="center">
          {CATEGORIES.map(cat => (
            <Tooltip key={cat.id} label={cat.name} placement="right" hasArrow bg="brown.800" color="brand.500">
              <MotionBox
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.95 }}
                onClick={() => setActiveCategory(cat.id)}
                cursor="pointer"
                position="relative"
              >
                <Flex
                  w={{ base: '50px', md: '60px' }}
                  h={{ base: '50px', md: '60px' }}
                  align="center"
                  justify="center"
                  borderRadius="20px"
                  bg={activeCategory === cat.id ? 'brand.500' : 'transparent'}
                  color={activeCategory === cat.id ? 'brown.900' : 'brown.200'}
                  transition="all 0.3s ease"
                  _hover={{
                    bg: activeCategory === cat.id ? 'brand.500' : 'brown.800',
                    color: activeCategory === cat.id ? 'brown.900' : 'brand.500',
                  }}
                  boxShadow={activeCategory === cat.id ? '0 10px 20px rgba(255, 193, 7, 0.3)' : 'none'}
                >
                  {cat.icon}
                </Flex>
                {activeCategory === cat.id && (
                  <Box
                    position="absolute"
                    right={{ base: 'auto', md: '-18px' }}
                    bottom={{ base: '-10px', md: 'auto' }}
                    top={{ base: 'auto', md: '50%' }}
                    transform={{ base: 'translateX(-50%)', md: 'translateY(-50%)' }}
                    left={{ base: '50%', md: 'auto' }}
                    w={{ base: '20px', md: '4px' }}
                    h={{ base: '4px', md: '20px' }}
                    bg="brand.500"
                    borderRadius="full"
                  />
                )}
              </MotionBox>
            </Tooltip>
          ))}
        </Flex>
      </Flex>

      {/* Main Content */}
      <Flex flex={1} direction="column" p={{ base: 4, md: 6 }} zIndex={1} overflow="hidden" mb={{ base: '80px', md: 0 }}>
        {/* Header */}
        <Flex justify="space-between" align="center" mb={8} px={2} direction={{ base: 'column', md: 'row' }} gap={4}>
          <Box w="full">
            <Heading size="xl" color="brown.900" fontWeight="800" letterSpacing="tight">
              {CATEGORIES.find(c => c.id === activeCategory)?.name}
              <Text as="span" color="brand.500">.</Text>
            </Heading>
            <Text color="gray.500" fontSize="lg" mt={1}>
              {filteredItems.length} tasty items available
            </Text>
          </Box>
          
          <InputGroup w={{ base: 'full', md: '400px' }} size="lg">
            <InputLeftElement pointerEvents="none" h="full">
              <SearchIcon color="gray.400" />
            </InputLeftElement>
            <Input
              placeholder="Search for food..."
              bg="white"
              border="none"
              borderRadius="20px"
              h="55px"
              fontSize="md"
              boxShadow="0 4px 20px rgba(0,0,0,0.03)"
              _focus={{ boxShadow: '0 4px 25px rgba(255, 193, 7, 0.15)', bg: 'white' }}
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </InputGroup>
        </Flex>

        {/* Menu Grid */}
        <Box flex={1} overflowY="auto" px={2} pb={4} css={{
          '&::-webkit-scrollbar': { width: '6px' },
          '&::-webkit-scrollbar-track': { background: 'transparent' },
          '&::-webkit-scrollbar-thumb': { background: '#E0E0E0', borderRadius: '3px' },
        }}>
          <AnimatePresence mode='wait'>
            <MotionGrid
              key={activeCategory}
              templateColumns={{ base: "repeat(auto-fill, minmax(160px, 1fr))", md: "repeat(auto-fill, minmax(260px, 1fr))" }}
              gap={{ base: 4, md: 8 }}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -20 }}
              transition={{ duration: 0.3 }}
            >
              {filteredItems.map((item, index) => (
                <MotionBox
                  key={item.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.05 }}
                  whileHover={{ y: -8 }}
                >
                  <Flex
                    direction="column"
                    bg="white"
                    borderRadius="30px"
                    overflow="hidden"
                    boxShadow="0 10px 30px rgba(0,0,0,0.05)"
                    h={{ base: "280px", md: "360px" }}
                    position="relative"
                    role="group"
                  >
                    <Box h={{ base: "140px", md: "200px" }} overflow="hidden" position="relative">
                      <Image 
                        src={item.image} 
                        alt={item.name} 
                        w="100%" 
                        h="100%" 
                        objectFit="cover"
                        transition="transform 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94)"
                        _groupHover={{ transform: 'scale(1.1)' }}
                      />
                    </Box>
                    
                    <Flex direction="column" p={{ base: 4, md: 6 }} flex={1} justify="space-between">
                      <Box>
                        <Heading size={{ base: "sm", md: "md" }} mb={2} color="brown.900" fontWeight="700">
                          {item.name}
                        </Heading>
                        <Text fontSize="sm" color="gray.500" noOfLines={2} lineHeight="tall" display={{ base: 'none', md: 'block' }}>
                          {item.desc}
                        </Text>
                      </Box>
                      
                      <Flex justify="space-between" align="center" mt={2}>
                        <Text fontSize={{ base: "xl", md: "2xl" }} fontWeight="800" color="brown.900">
                          <Text as="span" fontSize="lg" color="brand.500" mr={1}>RM</Text>
                          {item.price}
                        </Text>
                        <IconButton
                          icon={<AddIcon />}
                          isRound
                          bg="brown.900"
                          color="brand.500"
                          size={{ base: "md", md: "lg" }}
                          _hover={{ bg: 'brand.500', color: 'brown.900', transform: 'scale(1.1)' }}
                          _active={{ transform: 'scale(0.95)' }}
                          onClick={() => addToCart(item)}
                          aria-label="Add to cart"
                          boxShadow="lg"
                        />
                      </Flex>
                    </Flex>
                  </Flex>
                  </MotionBox>
                ))}
              </MotionGrid>
            </AnimatePresence>
        </Box>
      </Flex>

      {/* Desktop Order Summary Sidebar */}
      <Flex
        direction="column"
        w="420px"
        h="96vh"
        m="2vh"
        zIndex={10}
        position="relative"
        display={{ base: 'none', lg: 'flex' }}
      >
        <CartSection 
          cart={cart} 
          updateQty={updateQty} 
          removeFromCart={removeFromCart} 
          subtotal={subtotal} 
          tax={tax} 
          total={total} 
          onCheckout={handleCheckout}
        />
      </Flex>

      {/* Mobile Cart Drawer */}
      <Drawer isOpen={isOpen} placement="right" onClose={onClose} size="md">
        <DrawerOverlay />
        <DrawerContent bg="transparent" boxShadow="none">
          <DrawerCloseButton zIndex={20} color="brown.900" />
          <DrawerBody p={0}>
             <CartSection 
              cart={cart} 
              updateQty={updateQty} 
              removeFromCart={removeFromCart} 
              subtotal={subtotal} 
              tax={tax} 
              total={total} 
              onCheckout={handleCheckout}
            />
          </DrawerBody>
        </DrawerContent>
      </Drawer>

      {/* Mobile Cart FAB */}
      <IconButton
        icon={
          <Box position="relative">
            <ShoppingCart size={24} />
            {cart.length > 0 && (
              <Badge
                position="absolute"
                top="-8px"
                right="-8px"
                bg="brand.500"
                color="brown.900"
                borderRadius="full"
                boxSize="20px"
                display="flex"
                alignItems="center"
                justifyContent="center"
                fontSize="xs"
              >
                {cart.length}
              </Badge>
            )}
          </Box>
        }
        position="fixed"
        bottom="100px"
        right="20px"
        size="lg"
        isRound
        bg="brown.900"
        color="brand.500"
        boxShadow="0 10px 20px rgba(0,0,0,0.2)"
        zIndex={99}
        onClick={onOpen}
        display={{ base: 'flex', lg: 'none' }}
        _hover={{ transform: 'scale(1.1)' }}
        _active={{ transform: 'scale(0.95)' }}
      />
    </Flex>
  );
};

export default OrderPage;
