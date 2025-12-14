import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
  Radio,
  RadioGroup,
  Stack,
  Checkbox,
  CheckboxGroup,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter
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
  Star,
  ArrowLeft,
  Drumstick
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import logo from '../assets/logo.png';
import { createOrder } from '../api/orders';
import { createPayment } from '../api/payments';
import { getAllItems, getItemRemarks } from '../api/items';
import { baseURL } from '../api/client';
import SecureImage from '../components/SecureImage';

// Motion Components
const MotionBox = motion.create(Box);
const MotionFlex = motion.create(Flex);
const MotionGrid = motion.create(Grid);

// Mock Data
const CATEGORIES = [
  { id: 'BURGER', name: 'Burgers', icon: <Hamburger size={24} /> },
  { id: 'FRIED_CHICKEN', name: 'Fried Chicken', icon: <Drumstick size={24} /> },
  { id: 'BEVERAGES', name: 'Beverages', icon: <CupSoda size={24} /> },
  { id: 'DESSERTS', name: 'Desserts', icon: <IceCream size={24} /> },
  { id: 'CONDIMENTS', name: 'Condiments', icon: <Popcorn size={24} /> },
];

const CartSection = ({ cart, updateQty, removeFromCart, total, onCheckout }) => (
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
                key={item.internalId}
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
                  <SecureImage
                    src={item.image}
                    boxSize="65px"
                    borderRadius="xl"
                    objectFit="cover"
                    mr={4}
                    boxShadow="sm"
                  />
                  <Box flex={1}>
                    <Text fontWeight="bold" color="brown.900" fontSize="md">{item.name}</Text>
                    {item.remarks && item.remarks.length > 0 && (
                      <Text fontSize="xs" color="gray.500">{item.remarks.join(', ')}</Text>
                    )}
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
                      onClick={() => item.qty > 1 ? updateQty(item.internalId, -1) : removeFromCart(item.internalId)}
                      aria-label="Decrease"
                      _hover={{ bg: 'gray.100' }}
                    />
                    <Text fontWeight="bold" fontSize="sm" w="30px" textAlign="center">{item.qty}</Text>
                    <IconButton
                      icon={<AddIcon />}
                      size="xs"
                      variant="ghost"
                      color="brown.900"
                      onClick={() => updateQty(item.internalId, 1)}
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
  const [activeCategory, setActiveCategory] = useState('BURGER');
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [remarksOptions, setRemarksOptions] = useState({});
  const { isOpen, onOpen, onClose } = useDisclosure();

  // Remarks Modal State
  const { isOpen: isRemarkOpen, onOpen: onRemarkOpen, onClose: onRemarkClose } = useDisclosure();
  const [selectedItem, setSelectedItem] = useState(null);
  const [remarkRadio, setRemarkRadio] = useState('');
  const [remarkCheckbox, setRemarkCheckbox] = useState([]);
  const [modalQty, setModalQty] = useState(1);

  // Payment Modal State
  const { isOpen: isPaymentOpen, onOpen: onPaymentOpen, onClose: onPaymentClose } = useDisclosure();

  const theme = useTheme();
  const toast = useToast();
  const navigate = useNavigate();

  React.useEffect(() => {
    fetchData();
  }, []);

  const getImageUrl = (imagePath) => {
    if (!imagePath) return 'https://via.placeholder.com/300?text=No+Image';

    // Strip existing domain path if present to ensure we use the dynamic baseURL
    let cleanPath = imagePath;
    if (imagePath.includes('/uploads/')) {
      cleanPath = imagePath.split('/uploads/')[1];
    }

    return `${baseURL}/uploads/${cleanPath}`;
  };

  const fetchData = async () => {
    fetchMenuItems();
    fetchRemarks();
  };

  const fetchRemarks = async () => {
    try {
      const data = await getItemRemarks();
      // Process backend map into frontend options format
      const options = {};
      Object.keys(data).forEach(category => {
        const remarksList = data[category];
        let type = 'checkbox';
        let title = `Customize your ${category.replace('_', ' ').toLowerCase()}`;

        if (category === 'BEVERAGES') {
          type = 'radio';
          title = 'Drink Preferences';
        } else if (category === 'BURGER') {
          title = 'Customize your Burger';
        }

        options[category] = {
          type: type,
          title: title,
          options: remarksList // List of formatted strings from backend e.g. "EXTRA LETTUCE"
        };
      });
      setRemarksOptions(options);
    } catch (error) {
      console.error("Failed to fetch remarks", error);
    }
  };

  const fetchMenuItems = async () => {
    try {
      const data = await getAllItems();
      const mappedItems = data.map(item => ({
        id: item.itemId,
        category: item.itemCategory,
        name: item.itemName,
        price: item.itemPrice,
        image: getImageUrl(item.itemImage),
        desc: `Delicious ${item.itemCategory.toLowerCase().replace('_', ' ')}`
      }));
      setMenuItems(mappedItems);
    } catch (error) {
      toast({
        title: "Error fetching menu",
        description: error.message,
        status: "error",
        duration: 3000,
      });
    }
  };

  const handleCheckoutClick = () => {
    onPaymentOpen();
  };

  const handlePayment = async (method) => {
    onPaymentClose();
    try {
      // 1. Create Payment
      const paymentData = {
        paymentAmount: total,
        paymentType: method
      };

      const paymentResponse = await createPayment(paymentData);
      const paymentId = paymentResponse.paymentId;

      // 2. Create Order
      const orderData = {
        orderItems: cart.map(item => ({
          itemId: item.id.toString(),
          itemName: item.name,
          unitPrice: item.price,
          quantity: item.qty,
          lineTotal: parseFloat((item.price * item.qty).toFixed(2)),
          remarks: item.remarks || []
        })),
        paymentId: paymentId
      };

      const data = await createOrder(orderData);

      toast({
        title: "Order Placed Successfully!",
        description: `Order ID: ${data.orderId}`,
        status: "success",
        duration: 5000,
        isClosable: true,
        position: "top",
      });
      setCart([]);
    } catch (error) {
      console.error("Order Error:", error);
      toast({
        title: "Checkout Failed",
        description: error.response?.data?.message || "Something went wrong during checkout.",
        status: "error",
        duration: 5000,
        isClosable: true,
        position: "top",
      });
    }
  };



  const filteredItems = menuItems.filter(item =>
    item.category === activeCategory &&
    item.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const initiateAddToCart = (item) => {
    setSelectedItem(item);

    // Reset selections
    setRemarkRadio('Regular');
    setRemarkCheckbox([]);
    setModalQty(1);

    onRemarkOpen();
  };

  const confirmAddToCart = () => {
    let finalRemarks = [];
    const config = remarksOptions[selectedItem.category];

    if (config) {
      if (config.type === 'radio' && remarkRadio) {
        finalRemarks.push(remarkRadio);
      } else if (config.type === 'checkbox') {
        finalRemarks = [...remarkCheckbox];
      }
    }

    setCart(prev => {
      // Create a unique ID based on item ID and remarks to separate variations
      // We use internalId for frontend tracking
      const newItem = {
        ...selectedItem,
        qty: modalQty,
        remarks: finalRemarks,
        internalId: selectedItem.id + '-' + JSON.stringify(finalRemarks) + '-' + Date.now()
      };

      const existing = prev.find(i => i.id === selectedItem.id && JSON.stringify(i.remarks) === JSON.stringify(finalRemarks));

      if (existing) {
        return prev.map(i => i.internalId === existing.internalId ? { ...i, qty: i.qty + modalQty } : i);
      }
      return [...prev, newItem];
    });

    onRemarkClose();
    toast({ title: "Added to cart", status: "success", duration: 1000 });
  };

  const updateQty = (internalId, delta) => {
    setCart(prev => prev.map(item => {
      if (item.internalId === internalId) {
        const newQty = Math.max(0, item.qty + delta);
        return { ...item, qty: newQty };
      }
      return item;
    }).filter(item => item.qty > 0));
  };

  const removeFromCart = (internalId) => {
    setCart(prev => prev.filter(item => item.internalId !== internalId));
  };

  const total = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);

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
                    h="100%"
                    position="relative"
                    role="group"
                  >
                    <Box h={{ base: "140px", md: "200px" }} overflow="hidden" position="relative" flexShrink={0}>
                      <SecureImage
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

                      </Box>

                      <Flex justify="space-between" align="center" mt={2}>
                        <Text fontSize={{ base: "xl", md: "2xl" }} fontWeight="800" color="brown.900">
                          <Text as="span" fontSize="lg" color="brand.500" mr={1}>RM</Text>
                          {item.price.toFixed(2)}
                        </Text>
                        <IconButton
                          icon={<AddIcon />}
                          isRound
                          bg="brown.900"
                          color="brand.500"
                          size={{ base: "md", md: "lg" }}
                          _hover={{ bg: 'brand.500', color: 'brown.900', transform: 'scale(1.1)' }}
                          _active={{ transform: 'scale(0.95)' }}
                          onClick={() => initiateAddToCart(item)}
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
          total={total}
          onCheckout={handleCheckoutClick}
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
              total={total}
              onCheckout={handleCheckoutClick}
            />
          </DrawerBody>
        </DrawerContent>
      </Drawer>

      {/* Remarks Modal */}
      <Modal isOpen={isRemarkOpen} onClose={onRemarkClose} isCentered>
        <ModalOverlay />
        <ModalContent borderRadius="20px">
          <ModalHeader color="brown.900">
            {selectedItem?.name}
            {selectedItem && (
              <Text fontSize="md" color="brand.600" fontWeight="bold">RM {selectedItem.price.toFixed(2)}</Text>
            )}
          </ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            {selectedItem && (
              <Box mb={4} borderRadius="xl" overflow="hidden" height="200px">
                <SecureImage src={selectedItem.image} alt={selectedItem.name} width="100%" height="100%" objectFit="cover" />
              </Box>
            )}
            {selectedItem && remarksOptions[selectedItem.category] ? (
              <Stack spacing={4}>
                <Text fontWeight="medium" color="gray.600">{remarksOptions[selectedItem.category].title}</Text>
                {remarksOptions[selectedItem.category].type === 'radio' ? (
                  <RadioGroup onChange={setRemarkRadio} value={remarkRadio}>
                    <Stack spacing={2}>
                      {remarksOptions[selectedItem.category].options.map(opt => (
                        <Radio key={opt} value={opt} colorScheme="orange" size="lg">
                          <Text fontSize="md">{opt.replace(/_/g, ' ')}</Text>
                        </Radio>
                      ))}
                    </Stack>
                  </RadioGroup>
                ) : (
                  <CheckboxGroup colorScheme="orange" value={remarkCheckbox} onChange={setRemarkCheckbox}>
                    <Stack spacing={2}>
                      {remarksOptions[selectedItem.category].options.map(opt => (
                        <Checkbox key={opt} value={opt} size="lg">
                          <Text fontSize="md">{opt.replace(/_/g, ' ')}</Text>
                        </Checkbox>
                      ))}
                    </Stack>
                  </CheckboxGroup>
                )}
              </Stack>
            ) : null}

            {/* Quantity Selector */}
            <Flex align="center" justify="space-between" mt={6} pt={4} borderTop="1px solid" borderColor="gray.100">
              <Text fontWeight="bold" color="brown.900">Quantity</Text>
              <HStack spacing={4}>
                <IconButton
                  icon={<MinusIcon />}
                  size="sm"
                  variant="outline"
                  colorScheme="brown"
                  onClick={() => setModalQty(Math.max(1, modalQty - 1))}
                  isDisabled={modalQty <= 1}
                  borderRadius="full"
                />
                <Text fontWeight="bold" fontSize="lg" w="50px" textAlign="center">{modalQty}</Text>
                <IconButton
                  icon={<AddIcon />}
                  size="sm"
                  variant="solid"
                  bg="brown.900"
                  color="brand.500"
                  _hover={{ bg: 'brown.800' }}
                  onClick={() => setModalQty(modalQty + 1)}
                  borderRadius="full"
                />
              </HStack>
            </Flex>
          </ModalBody>
          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onRemarkClose}>Cancel</Button>
            <Button bg="brown.900" color="brand.500" _hover={{ bg: 'brown.800' }} onClick={confirmAddToCart}>
              Add to Cart
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Payment Selection Modal */}
      <Modal isOpen={isPaymentOpen} onClose={onPaymentClose} isCentered size="md">
        <ModalOverlay />
        <ModalContent borderRadius="2xl" p={4}>
          <ModalHeader textAlign="center" fontSize="2xl" color="brown.900">Select Payment Method</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <VStack spacing={4}>
              <Button
                w="full"
                h="80px"
                colorScheme="green"
                variant="outline"
                borderWidth="2px"
                _hover={{ bg: 'green.50', transform: 'scale(1.02)' }}
                onClick={() => handlePayment('CASH')}
              >
                <VStack spacing={1}>
                  <Text fontSize="xl" fontWeight="bold">CASH</Text>
                  <Text fontSize="sm">Pay at counter</Text>
                </VStack>
              </Button>
              <Button
                w="full"
                h="80px"
                colorScheme="blue"
                variant="outline"
                borderWidth="2px"
                _hover={{ bg: 'blue.50', transform: 'scale(1.02)' }}
                onClick={() => handlePayment('CREDIT_CARD')}
              >
                <VStack spacing={1}>
                  <Text fontSize="xl" fontWeight="bold">CREDIT CARD</Text>
                  <Text fontSize="sm">Visa, Mastercard, etc.</Text>
                </VStack>
              </Button>
            </VStack>
          </ModalBody>
          <ModalFooter justify="center">
            <Button variant="ghost" onClick={onPaymentClose}>Cancel</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

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
