import React from 'react';
import { Box, Heading, Table, Thead, Tbody, Tr, Th, Td, Button, HStack, Badge, Image } from '@chakra-ui/react';
import { AddIcon, EditIcon, DeleteIcon } from '@chakra-ui/icons';

const ItemManagement = () => {
  return (
    <Box>
      <HStack justify="space-between" mb={6}>
        <Heading size="lg" color="brown.900">Item Management</Heading>
        <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900">
          Add New Item
        </Button>
      </HStack>

      <Box bg="white" borderRadius="xl" boxShadow="sm" overflowX="auto">
        <Table variant="simple">
          <Thead>
            <Tr>
              <Th>Image</Th>
              <Th>Name</Th>
              <Th>Category</Th>
              <Th isNumeric>Price (RM)</Th>
              <Th>Status</Th>
              <Th>Actions</Th>
            </Tr>
          </Thead>
          <Tbody>
            <Tr>
              <Td>
                <Image 
                  src="https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=100&q=60" 
                  boxSize="50px" 
                  borderRadius="md" 
                  objectFit="cover" 
                />
              </Td>
              <Td fontWeight="medium">Teddy Classic Burger</Td>
              <Td>Burgers</Td>
              <Td isNumeric>12.90</Td>
              <Td><Badge colorScheme="green">Available</Badge></Td>
              <Td>
                <HStack>
                  <Button size="sm" leftIcon={<EditIcon />}>Edit</Button>
                  <Button size="sm" colorScheme="red" variant="ghost"><DeleteIcon /></Button>
                </HStack>
              </Td>
            </Tr>
             <Tr>
              <Td>
                <Image 
                  src="https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=100&q=60" 
                  boxSize="50px" 
                  borderRadius="md" 
                  objectFit="cover" 
                />
              </Td>
              <Td fontWeight="medium">Margherita Pizza</Td>
              <Td>Pizza</Td>
              <Td isNumeric>22.00</Td>
              <Td><Badge colorScheme="green">Available</Badge></Td>
              <Td>
                <HStack>
                  <Button size="sm" leftIcon={<EditIcon />}>Edit</Button>
                  <Button size="sm" colorScheme="red" variant="ghost"><DeleteIcon /></Button>
                </HStack>
              </Td>
            </Tr>
          </Tbody>
        </Table>
      </Box>
    </Box>
  );
};

export default ItemManagement;
