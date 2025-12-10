import React, { useState } from 'react';
import {
  Box,
  Flex,
  Heading,
  Text,
  Input,
  Button,
  VStack,
  Image,
  FormControl,
  FormLabel,
  useToast,
  InputGroup,
  InputRightElement,
  IconButton,
  Container
} from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
import { motion } from 'framer-motion';
import logo from '../assets/logo.png';
import { useNavigate } from 'react-router-dom';
import { login } from '../api/auth';

const MotionBox = motion.create(Box);

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const toast = useToast();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const user = await login({ email, password });

      localStorage.setItem('user', JSON.stringify(user));

      toast({
        title: "Login Successful",
        description: `Welcome back, ${user.name || 'Employee'}!`,
        status: "success",
        duration: 3000,
        isClosable: true,
        position: "top",
      });
      navigate('/dashboard');
    } catch (error) {
      toast({
        title: "Login Failed",
        description: error.message || "Invalid email or password",
        status: "error",
        duration: 3000,
        isClosable: true,
        position: "top",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Flex
      minH="100vh"
      w="100vw"
      bg="brown.50"
      align="center"
      justify="center"
      position="relative"
      overflow="hidden"
    >
      {/* Background Decoration */}
      <Box
        position="absolute"
        top="-10%"
        right="-5%"
        w="500px"
        h="500px"
        bg="brand.200"
        borderRadius="full"
        filter="blur(80px)"
        opacity={0.4}
        zIndex={0}
      />
      <Box
        position="absolute"
        bottom="-10%"
        left="-5%"
        w="400px"
        h="400px"
        bg="brown.200"
        borderRadius="full"
        filter="blur(80px)"
        opacity={0.4}
        zIndex={0}
      />

      <Container maxW="md" zIndex={1}>
        <MotionBox
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          bg="white"
          p={8}
          borderRadius="3xl"
          boxShadow="0 20px 50px rgba(62, 39, 35, 0.1)"
        >
          <VStack spacing={6} align="stretch">
            <Flex direction="column" align="center" mb={2}>
              <Box p={3} bg="brand.500" borderRadius="full" boxShadow="lg" mb={4}>
                <Image src={logo} boxSize="60px" objectFit="contain" />
              </Box>
              <Heading color="brown.900" size="lg">Welcome Back</Heading>
              <Text color="gray.500" fontSize="sm">Sign in to manage orders</Text>
            </Flex>

            <form onSubmit={handleLogin}>
              <VStack spacing={4}>
                <FormControl isRequired>
                  <FormLabel color="brown.900">Email Address</FormLabel>
                  <Input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="employee@teddybite.com"
                    size="lg"
                    borderRadius="xl"
                    bg="gray.50"
                    border="none"
                    _focus={{ bg: 'white', boxShadow: '0 0 0 2px #FFC107' }}
                  />
                </FormControl>

                <FormControl isRequired>
                  <FormLabel color="brown.900">Password</FormLabel>
                  <InputGroup size="lg">
                    <Input
                      type={showPassword ? "text" : "password"}
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="••••••••"
                      borderRadius="xl"
                      bg="gray.50"
                      border="none"
                      _focus={{ bg: 'white', boxShadow: '0 0 0 2px #FFC107' }}
                    />
                    <InputRightElement>
                      <IconButton
                        variant="ghost"
                        icon={showPassword ? <ViewOffIcon /> : <ViewIcon />}
                        onClick={() => setShowPassword(!showPassword)}
                        aria-label={showPassword ? "Hide password" : "Show password"}
                      />
                    </InputRightElement>
                  </InputGroup>
                </FormControl>

                <Button
                  type="submit"
                  w="full"
                  size="lg"
                  bg="brown.900"
                  color="brand.500"
                  isLoading={isLoading}
                  loadingText="Signing in..."
                  _hover={{
                    bg: 'brown.800',
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg'
                  }}
                  _active={{ transform: 'translateY(0)' }}
                  mt={4}
                >
                  Sign In
                </Button>
              </VStack>
            </form>

            <Text textAlign="center" fontSize="sm" color="gray.400">
              Forgot your password? <Text as="span" color="brand.600" fontWeight="bold" cursor="pointer">Contact Admin</Text>
            </Text>
          </VStack>
        </MotionBox>
      </Container>
    </Flex>
  );
};

export default LoginPage;
